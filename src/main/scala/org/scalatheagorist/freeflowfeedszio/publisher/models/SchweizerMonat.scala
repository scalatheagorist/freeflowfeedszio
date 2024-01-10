package org.scalatheagorist.freeflowfeedszio.publisher.models

import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import org.scalatheagorist.freeflowfeedszio.models.Article
import org.scalatheagorist.freeflowfeedszio.models.HtmlResponse
import org.scalatheagorist.freeflowfeedszio.models.Feed
import org.scalatheagorist.freeflowfeedszio.publisher.Props.*
import zio.prelude.AssociativeBothTuple2Ops
import zio.stream.ZStream

case object SchweizerMonat extends PublisherModel:
    override def toFeedStream(htmlResponse: HtmlResponse): ZStream[Any, Throwable, Feed] = ZStream.fromIterable {
        List
            .from(parseString(htmlResponse.response) >> elementList(".teaser__link"))
            .flatMap { elem =>
                val title  = elem >?> attr("title")
                val href   = elem >?> attr("href")
                val author = "SchweizerMonat"

                (href, title).mapN { (link, title) =>
                    Feed(author, Article(title, link), Publisher.SCHWEIZER_MONAT, Lang.DE)
                }
            }
    }
