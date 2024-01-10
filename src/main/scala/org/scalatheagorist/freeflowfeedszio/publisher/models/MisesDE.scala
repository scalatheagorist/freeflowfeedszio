package org.scalatheagorist.freeflowfeedszio.publisher.models

import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import org.scalatheagorist.freeflowfeedszio.models.Article
import org.scalatheagorist.freeflowfeedszio.models.HtmlResponse
import org.scalatheagorist.freeflowfeedszio.models.Feed
import org.scalatheagorist.freeflowfeedszio.publisher.Props.*
import zio.prelude.AssociativeBothTuple3Ops
import zio.stream.ZStream

case object MisesDE extends PublisherModel:
    override def toFeedStream(htmlResponse: HtmlResponse): ZStream[Any, Throwable, Feed] = ZStream.fromIterable {
        List
            .from(parseString(htmlResponse.response) >> elementList(".pt-cv-content-item"))
            .flatMap { elem =>
                val author = (elem >?> element(".author") >?> text("span")).flatten
                val title  = (elem >?> element(".pt-cv-title") >?> text("a")).flatten
                val href   = (elem >?> element(".pt-cv-mask") >?> element("a") >?> attr("href")).flatten.flatten
                
                (author, href, title).mapN { (author, link, title) =>
                    Feed(author, Article(title, link), Publisher.MISESDE, Lang.DE)
                }
            }
    }
