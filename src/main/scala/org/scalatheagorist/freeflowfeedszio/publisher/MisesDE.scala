package org.scalatheagorist.freeflowfeedszio.publisher

import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import org.scalatheagorist.freeflowfeedszio.models.Article
import org.scalatheagorist.freeflowfeedszio.models.HtmlResponse
import org.scalatheagorist.freeflowfeedszio.models.RSSFeed
import zio.prelude.AssociativeBothTuple3Ops
import zio.stream.ZStream

case object MisesDE extends PublisherModel {
  override def toRSSFeedStream(htmlResponse: HtmlResponse): ZStream[Any, Throwable, RSSFeed] = ZStream.fromIterable {
    List
      .from(browser.parseString(htmlResponse.response) >> elementList(".pt-cv-content-item"))
      .flatMap { elem =>
        val author = (elem >?> element(".author") >?> text("span")).flatten
        val title = (elem >?> element(".pt-cv-title") >?> text("a")).flatten
        val href = (elem >?> element(".pt-cv-mask") >?> element("a") >?> attr("href")).flatten.flatten

        (author, href, title).mapN { (author, link, title) =>
          RSSFeed(author, Article(title, link), Publisher.MISESDE, Lang.DE)
        }
      }
  }
}
