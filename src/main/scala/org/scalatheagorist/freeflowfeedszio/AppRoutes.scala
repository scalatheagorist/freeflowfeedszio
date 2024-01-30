package org.scalatheagorist.freeflowfeedszio

import caliban.*
import caliban.interop.tapir
import caliban.interop.tapir.HttpInterpreter
import caliban.interop.tapir.schema
import caliban.schema.Annotations.GQLDescription
import caliban.schema.Annotations.GQLName
import caliban.schema.ArgBuilder.auto.*
import caliban.schema.Schema
import caliban.schema.Schema.auto.*
import org.scalatheagorist.freeflowfeedszio.AppRoutes.Args.*
import org.scalatheagorist.freeflowfeedszio.props.Props
import org.scalatheagorist.freeflowfeedszio.props.Props.Lang
import org.scalatheagorist.freeflowfeedszio.props.Props.Publisher
import org.scalatheagorist.freeflowfeedszio.services.FeedService
import org.scalatheagorist.freeflowfeedszio.view.IndexHtml
import zio.IO
import zio.Task
import zio.ZIO
import zio.ZLayer
import zio.http.Body
import zio.http.Header
import zio.http.Headers
import zio.http.HttpApp
import zio.http.MediaType
import zio.http.Response
import zio.http.Status
import zio.prelude.AssociativeBothTuple2Ops
import zio.stream.ZStream

import scala.util.Try

trait AppRoutes:
  def apply: GraphQL[Any]

object AppRoutes:
  sealed trait Args(page: Int)
  object Args {
    case class PropsArgs(props: Props, page: Int)  extends Args(page)
    case class AllArgs(page: Int)                  extends Args(page)
    case class SearchArgs(term: String, page: Int) extends Args(page)
  }

  @GQLName("Response")
  case class ResponseQL(result: String)

  case class Queries(
      @GQLDescription("Return all articles by publisher name")
      props: Option[PropsArgs] => Task[ResponseQL],
      @GQLDescription("Return all articles")
      all: Option[AllArgs] => Task[ResponseQL],
      @GQLDescription("Return all articles filtered by search term")
      search: Option[SearchArgs] => Task[ResponseQL]
  )

  given Schema[Any, PropsArgs]  = Schema.gen
  given Schema[Any, AllArgs]    = Schema.gen
  given Schema[Any, SearchArgs] = Schema.gen
  given Schema[Any, ResponseQL] = Schema.gen
  given Schema[Any, Queries]    = Schema.gen

  val layer: ZLayer[Configuration & FeedService, Nothing, AppRoutes] =
    ZLayer {
      (ZIO.service[FeedService], ZIO.serviceWith[Configuration](_.pageSize)).mapN { (feedService, pageSize) =>
        new AppRoutes {
          override def apply: GraphQL[Any] =
            def query(page: Int, props: Option[Props], term: Option[String]): Task[ResponseQL] =
              val page0  = if (page < 0) 0 else page - 1
              val feeds  = feedService.getFeeds(page0, pageSize, props, term)
              val header = Header.ContentType(mediaType = MediaType.text.html)

              if page0 == 0 then
                feeds
                  .runFold("")(_ + _)
                  .tapError(err => ZIO.logWarning(s"could not create content: ${err.getMessage}"))
                  .map(IndexHtml(_))
                  .map(ResponseQL)
              else feeds.runFold("")(_ + _).map(ResponseQL)

            def matching[A <: Args](a: Option[A]): Task[ResponseQL] = a match {
              case Some(PropsArgs(props, page)) => query(page, Some(props), None)
              case Some(SearchArgs(term, page)) => query(page, None, Some(term))
              case Some(AllArgs(page))          => query(page, None, None)
              case _                            => ZIO.succeed(ResponseQL("NOT FOUND")) <* ZIO.logWarning(s"NOT FOUND")
            }

            graphQL(
              RootResolver(
                Queries(
                  publisherArgs => matching[PropsArgs](publisherArgs),
                  argsAll => matching[AllArgs](argsAll),
                  argsTerm => matching[SearchArgs](argsTerm)
                )
              )
            )
        }
      }
    }
