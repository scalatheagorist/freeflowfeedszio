package org.scalatheagorist.freeflowfeedszio.publisher.models

import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import org.scalatheagorist.freeflowfeedszio.models.Article
import org.scalatheagorist.freeflowfeedszio.models.HtmlResponse
import org.scalatheagorist.freeflowfeedszio.models.RSSFeed
import org.scalatheagorist.freeflowfeedszio.publisher.Category.*
import zio.prelude.AssociativeBothTuple2Ops
import zio.stream.ZStream

final case class Freiheitsfunken(prefix: String) extends PublisherModel:
  override def toRSSFeedStream(htmlResponse: HtmlResponse): ZStream[Any, Throwable, RSSFeed] = ZStream.fromIterable {
    List.from(parseString(htmlResponse.response) >> elementList("article")).flatMap { elem =>
      val article = elem >> element("article")
      val link    = article >?> element("a") >> attr("href")
      val title   = (article >?> elements("div") >?> elements("h2") >?> text("a")).flatten.flatten
      val author =
        (article >?> elements("div") >?> elements("p") >?> text("em")).flatten.flatten
          .getOrElse("Freiheitsfunken")
          .split("von ")
          .last

      (link, title).mapN { (link, title) =>
        val href = if (link.startsWith("https")) link else s"$prefix$link"
        RSSFeed(author, Article(title, href), Publisher.FREIHEITSFUNKEN, Lang.DE)
      }
    }
  }
