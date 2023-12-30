package org.scalatheagorist.freeflowfeedszio.view

import org.scalatheagorist.freeflowfeedszio.models.RSSFeed
import org.scalatheagorist.freeflowfeedszio.publisher.Category
import org.scalatheagorist.freeflowfeedszio.publisher.Category.Lang
import org.scalatheagorist.freeflowfeedszio.publisher.Category.Publisher
import zio.ULayer
import zio.ZLayer
import zio.stream.ZStream

trait RSSBuilder:
  def build[R, E](category: Option[Category])(
    stream: ZStream[R, E, RSSFeed]
  ): ZStream[R, E, String]

object RSSBuilder:
  val layer: ULayer[RSSBuilder] =
    ZLayer.succeed {
      new RSSBuilder:
        def build[R, E](category: Option[Category])(
          stream: ZStream[R, E, RSSFeed]
        ): ZStream[R, E, String] =
          (category match
            case None               => stream
            case Some(p: Publisher) => stream.filter(_.publisher == p)
            case Some(l: Lang)      => stream.filter(_.lang == l)
          ).flatMap(feed => ZStream.succeed(generateCards(feed)))
    }

  private def generateCards(rssFeed: RSSFeed): String =
    s"""
       |<div class="article-card">
       |    <div class="card mb-3 bg-primary text-white">
       |        <div class="card-body" onclick="window.open('${rssFeed.article.link}', '_blank');" style="cursor: pointer;">
       |          ${s"""<p>${rssFeed.author}</p>\n"""}
       |          ${s"""<p><span class="highlight-title">${rssFeed.article.title}</span></p>"""}
       |          ${s"""<p><strong>Link:</strong><br><a href="${rssFeed.article.link}" target="_blank">${rssFeed.article.link}</a></p>"""}
       |        </div>
       |    </div>
       |</div>
       |""".stripMargin
