package org.scalatheagorist.freeflowfeedszio.publisher

import cats.Show
import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import org.scalatheagorist.freeflowfeedszio.models.HtmlResponse
import org.scalatheagorist.freeflowfeedszio.models.RSSFeed
import org.scalatheagorist.freeflowfeedszio.publisher.Hosts.PublisherUrl
import org.scalatheagorist.freeflowfeedszio.util.RichURL.RichUrl
import zio.json.JsonDecoder
import zio.json.JsonEncoder
import zio.stream.ZStream

sealed trait Publisher extends Product with Serializable

object Publisher {
  case object EFMAGAZIN extends Publisher
  case object FREIHEITSFUNKEN extends Publisher
  case object MISESDE extends Publisher
  case object SCHWEIZERMONAT extends Publisher

  implicit val encoder: JsonEncoder[Publisher] = JsonEncoder.string.contramap {
    case EFMAGAZIN       => "EFMAGAZIN"
    case FREIHEITSFUNKEN => "FREIHEITSFUNKEN"
    case MISESDE         => "MISESDE"
    case SCHWEIZERMONAT  => "SCHWEIZERMONAT"
  }

  implicit val decoder: JsonDecoder[Publisher] = JsonDecoder.string.map {
    case "EFMAGAZIN"       => EFMAGAZIN
    case "FREIHEITSFUNKEN" => FREIHEITSFUNKEN
    case "MISESDE"         => MISESDE
    case "SCHWEIZERMONAT"  => SCHWEIZERMONAT
  }

  def toRSSFeedStream(htmlResponse: HtmlResponse, url: PublisherUrl): ZStream[Any, Throwable, RSSFeed] = htmlResponse.publisher match {
    case Publisher.EFMAGAZIN       => EfMagazin(url.url.toProtocolWithHost).toRSSFeedStream(htmlResponse)
    case Publisher.FREIHEITSFUNKEN => Freiheitsfunken(url.url.toProtocolWithHost).toRSSFeedStream(htmlResponse)
    case Publisher.MISESDE         => MisesDE.toRSSFeedStream(htmlResponse)
    case Publisher.SCHWEIZERMONAT  => SchweizerMonat.toRSSFeedStream(htmlResponse)
  }
}

trait PublisherModel {
  val browser: Browser = JsoupBrowser()
  def toRSSFeedStream(htmlResponse: HtmlResponse): ZStream[Any, Throwable, RSSFeed]
}

final case class PublisherHost(url: String, path: String, pageTo: Int, publisher: Publisher) {
  def toPublisherUrls: List[(Publisher, String)] = publisher match {
    case Publisher.SCHWEIZERMONAT => fromPaths
    case _ => fromPages
  }

  private def fromPages: List[(Publisher, String)] =
    (publisher, url) :: List.range(1, pageTo).map(page => s"$url$path$page").map(publisher -> _)

  private def fromPaths: List[(Publisher, String)] =
    path.split(", ").map(path => publisher -> s"$url$path").to(List)
}

object PublisherHost {
  implicit val show: Show[PublisherHost] = host =>
    s"""url:${host.url}, path:${host.path}, pageTo:${host.pageTo}, publisher:${host.publisher.toString}"""
}
