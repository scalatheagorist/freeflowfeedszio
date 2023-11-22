package org.scalatheagorist.freeflowfeedszio

import cats.implicits.catsSyntaxOptionId
import org.scalatheagorist.freeflowfeedszio.publisher.Lang
import org.scalatheagorist.freeflowfeedszio.publisher.Publisher
import org.scalatheagorist.freeflowfeedszio.services.RSSService
import zio.ZIO
import zio.ZLayer
import zio.http._

trait Routes {
  def apply: Http[Any, Nothing, Request, Response]
}

object Routes {
  val live: ZLayer[RSSService, Nothing, Routes] =
    ZLayer {
      ZIO.service[RSSService].map { rssService =>
        new Routes {
          override def apply: Http[Any, Nothing, Request, Response] = {
            def pullStream(publisher: Option[Publisher], lang: Option[Lang]): Response = {
              Header.ContentType.parse("text/html; charset=utf-8") match {
                case Right(header) =>
                  Response(body = Body.fromStream(rssService.generateFeeds(publisher, lang)), headers = Headers(header :: Nil))
                case Left(_) =>
                  Response(status = Status.InternalServerError)
              }
            }

            Http.collect[Request] {
              case Method.GET -> Root / "articles" / "efmagazin"       => pullStream(Publisher.EFMAGAZIN.some, None)
              case Method.GET -> Root / "articles" / "freiheitsfunken" => pullStream(Publisher.FREIHEITSFUNKEN.some, None)
              case Method.GET -> Root / "articles" / "misesde"         => pullStream(Publisher.MISESDE.some, None)
              case Method.GET -> Root / "articles" / "schweizermonat"  => pullStream(Publisher.SCHWEIZERMONAT.some, None)
              case Method.GET -> Root / "articles" / "german"          => pullStream(None, Lang.DE.some)
              case Method.GET -> Root / "articles" / "english"         => pullStream(None, Lang.EN.some)
              case _ => pullStream(None, None)
            }
          }
        }
      }
    }
}
