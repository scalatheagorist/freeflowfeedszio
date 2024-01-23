package org.scalatheagorist.freeflowfeedszio

import caliban.*
import caliban.interop.tapir.HttpInterpreter
import caliban.schema.ArgBuilder.auto.*
import caliban.schema.Schema.auto.*
import cats.implicits.catsSyntaxOptionId
import org.scalatheagorist.freeflowfeedszio.publisher.Props
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
  def apply: IO[CalibanError.ValidationError, GraphQLInterpreter[Any, CalibanError]]

object AppRoutes:
  private trait Args(page: Int)
  private case class PropsQueryArgs(props: Props, page: Int) extends Args(page)
  private case class AllArgs(page: Int)                      extends Args(page)
  private case class SearchArgs(term: String, page: Int)     extends Args(page)

  private case class Queries(
      publisher: Option[PropsQueryArgs] => Task[Response],
      all: Option[AllArgs] => Task[Response],
      term: Option[SearchArgs] => Task[Response]
  )

  val layer: ZLayer[Configuration & FeedService, Nothing, AppRoutes] =
    ZLayer {
      (ZIO.service[FeedService], ZIO.serviceWith[Configuration](_.pageSize)).mapN { (feedService, pageSize) =>
        new AppRoutes {
          override def apply: IO[CalibanError.ValidationError, GraphQLInterpreter[Any, CalibanError]] =
            def query(page: Int, props: Option[Props], term: Option[String]): Task[Response] =
              val page0  = if (page < 0) 0 else page - 1
              val feeds  = feedService.getFeeds(page0, pageSize, props, term)
              val header = Header.ContentType(mediaType = MediaType.text.html)

              if page0 == 0 then
                feeds
                  .runFold("")(_ + _)
                  .tapError(err => ZIO.logWarning(s"could not create content: ${err.getMessage}"))
                  .map(IndexHtml(_))
                  .map(index => Response(body = Body.fromString(index), headers = Headers(header :: Nil)))
              else ZIO.attempt(Response(body = Body.fromCharSequenceStream(feeds), headers = Headers(header :: Nil)))

            def matching[A <: Args](a: Option[A]): Task[Response] = a match {
              case Some(PropsQueryArgs(props, page)) => query(page, Some(props), None)
              case Some(SearchArgs(term, page))      => query(page, None, Some(term))
              case Some(AllArgs(page))               => query(page, None, None)
              case _                                 =>
                ZIO.logWarning(s"NOT FOUND") *> ZIO.succeed(Response(status = Status.NotFound))
            }

            graphQL(
              resolver = RootResolver(
                Queries(
                  publisherArgs => matching[PropsQueryArgs](publisherArgs),
                  argsAll => matching[AllArgs](argsAll),
                  argsTerm => matching[SearchArgs](argsTerm)
                )
              ),
              directives = Nil,
              schemaDirectives = Nil,
              schemaDescription = None
            ).interpreter
        }
      }
    }
