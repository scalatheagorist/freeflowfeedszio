package org.scalatheagorist.freeflowfeedszio.publisher

import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import org.scalatheagorist.freeflowfeedszio.models.Article
import org.scalatheagorist.freeflowfeedszio.models.HtmlResponse
import org.scalatheagorist.freeflowfeedszio.models.RSSFeed
import zio.prelude.AssociativeBothTuple2Ops
import zio.stream.ZStream

final case class EfMagazin(prefix: String) extends PublisherModel {
  override def toRSSFeedStream(htmlResponse: HtmlResponse): ZStream[Any, Throwable, RSSFeed] = ZStream.fromIterable {
    List.from(browser.parseString(htmlResponse.response) >> elementList("article")).flatMap { elem =>
      val article = elem >> element("article")
      val link = article >?> element("a") >> attr("href")
      val title = article >?> text("p")
      val author = (article >?> elements("p") >?> element("em") >?> text("a")).flatten.flatten.getOrElse("EfMagazin")

      (link, title).mapN { (link, title) =>
        val href = if (link.startsWith("https")) link else s"$prefix$link"
        RSSFeed(author, Article(title, href), Publisher.EFMAGAZIN, Lang.DE)
      }
    }
  }
}
