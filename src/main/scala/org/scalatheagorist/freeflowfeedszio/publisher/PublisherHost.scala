package org.scalatheagorist.freeflowfeedszio.publisher

import cats.Show
import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import org.scalatheagorist.freeflowfeedszio.models.HtmlResponse
import org.scalatheagorist.freeflowfeedszio.models.Feed
import org.scalatheagorist.freeflowfeedszio.publisher.Props.Publisher
import org.scalatheagorist.freeflowfeedszio.publisher.PublisherUrl
import org.scalatheagorist.freeflowfeedszio.util.RichURL.*
import zio.stream.ZStream

final case class PublisherHost(url: String, path: String, pageTo: Int, publisher: Publisher):
  def toPublisherUrls: List[(Publisher, String)] = publisher match
    case Publisher.SCHWEIZER_MONAT => fromPaths
    case _                         => fromPages

  private inline def fromPages: List[(Publisher, String)] =
    (publisher, url) :: List.range(1, pageTo).map(page => s"$url$path$page").map(publisher -> _)

  private inline def fromPaths: List[(Publisher, String)] =
    path.split(", ").map(path => publisher -> s"$url$path").to(List)

object PublisherHost:
  given show: Show[PublisherHost] with
    def show(host: PublisherHost): String =
      s"""url:${host.url}, path:${host.path}, pageTo:${host.pageTo}, publisher:${host.publisher.toString}"""

  given showList: Show[List[PublisherHost]] with
    def show(hosts: List[PublisherHost]): String =
      s"""${hosts.map(p => s"\n    $p").mkString}"""
