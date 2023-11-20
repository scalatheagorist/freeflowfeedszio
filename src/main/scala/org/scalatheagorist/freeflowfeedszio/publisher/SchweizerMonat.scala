package org.scalatheagorist.freeflowfeedszio.publisher

import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import org.scalatheagorist.freeflowfeedszio.models.Article
import org.scalatheagorist.freeflowfeedszio.models.HtmlResponse
import org.scalatheagorist.freeflowfeedszio.models.RSSFeed
import zio.prelude.AssociativeBothTuple2Ops
import zio.stream.ZStream

case object SchweizerMonat extends PublisherModel {
  override def toRSSFeedStream(htmlResponse: HtmlResponse): ZStream[Any, Throwable, RSSFeed] = ZStream.fromIterable {
    List
      .from(browser.parseString(htmlResponse.response) >> elementList(".teaser__link"))
      .flatMap { elem =>
        val title = elem >?> attr("title")
        val href = elem >?> attr("href")
        val author = "SchweizerMonat"

        (href, title).mapN { (link, title) =>
          RSSFeed(author, Article(title, link), Publisher.SCHWEIZERMONAT, Lang.DE)
        }
      }
  }
}
