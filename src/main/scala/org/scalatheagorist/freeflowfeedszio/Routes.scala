package org.scalatheagorist.freeflowfeedszio

import cats.implicits.catsSyntaxOptionId
import org.scalatheagorist.freeflowfeedszio.publisher.Lang
import org.scalatheagorist.freeflowfeedszio.publisher.Publisher
import org.scalatheagorist.freeflowfeedszio.services.RSSService
import org.scalatheagorist.freeflowfeedszio.view.IndexHtml
import zio.Task
import zio.ZIO
import zio.ZLayer
import zio.http._

import scala.util.Try

trait Routes {
  def apply: Http[Any, Throwable, Request, Response]
}

object Routes {
  private final val pageSize = 50

  val layer: ZLayer[RSSService, Nothing, Routes] =
    ZLayer {
      ZIO.service[RSSService].map { rssService =>
        new Routes {
          override def apply: Http[Any, Throwable, Request, Response] = {
            def pullStream(
              page: Option[String],
              pageSize: Option[Int],
              publisher: Option[Publisher],
              lang: Option[Lang],
              term: Option[String]
            ): Task[Response] = {
              val page0 = page.flatMap(p => Try(p.toInt - 1).toOption).getOrElse(0)
              val feeds = rssService.getFeeds(page0, pageSize.getOrElse(100000), publisher, lang, term)

              Header.ContentType.parse("text/html; charset=utf-8") match {
                case Right(header) if page0 == 0 =>
                  feeds
                    .runFold("")(_ + _)
                    .tapError(err => ZIO.logWarning(s"could not create content: ${err.getMessage}"))
                    .map(IndexHtml(_))
                    .map(index => Response(body = Body.fromString(index), headers = Headers(header :: Nil)))
                case Right(header) =>
                  ZIO.attempt(Response(body = Body.fromStream(feeds), headers = Headers(header :: Nil)))
                case Left(_) =>
                  ZIO.succeed(Response(status = Status.InternalServerError))
              }
            }

            Http.collectZIO[Request] {
              case Method.GET -> Root / "efmagazin" / page =>
                ZIO.logInfo(s"GET : /efmagazin/$page") *>
                  pullStream(Some(page), Some(pageSize), Publisher.EFMAGAZIN.some, None, None)
              case Method.GET -> Root / "freiheitsfunken" / page =>
                ZIO.logInfo(s"GET : /freiheitsfunken/$page") *>
                  pullStream(Some(page), Some(pageSize), Publisher.FREIHEITSFUNKEN.some, None, None)
              case Method.GET -> Root / "misesde" / page =>
                ZIO.logInfo(s"GET : /misesde/$page") *>
                  pullStream(Some(page), Some(pageSize), Publisher.MISESDE.some, None, None)
              case Method.GET -> Root / "schweizermonat" / page =>
                ZIO.logInfo(s"GET : /schweizermonat/$page") *>
                  pullStream(Some(page), Some(pageSize), Publisher.SCHWEIZER_MONAT.some, None, None)
              case Method.GET -> Root / "german" / page =>
                ZIO.logInfo(s"GET : /german/$page") *>
                  pullStream(Some(page), Some(pageSize), None, Lang.DE.some, None)
              case Method.GET -> Root / "english" / page =>
                ZIO.logInfo(s"GET : /english/$page") *>
                  pullStream(Some(page), Some(pageSize), None, Lang.EN.some, None)
              case req@Method.GET -> Root / "search" =>
                val query = req.url.queryParams.get("term").map(_.mkString)
                ZIO.logInfo(s"GET : /search/${query.mkString}") *>
                  pullStream(None, None, None, None, query)
              case Method.GET -> Root / "articles" / page =>
                ZIO.logInfo(s"GET : /articles/$page") *>
                  pullStream(Some(page), Some(pageSize), None, None, None)
              case _ =>
                ZIO.logWarning(s"GET : NOT FOUND") *> ZIO.succeed(Response(status = Status.NotFound))
            }
          }
        }
      }
    }
}
