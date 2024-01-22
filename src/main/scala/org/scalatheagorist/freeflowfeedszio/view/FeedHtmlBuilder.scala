package org.scalatheagorist.freeflowfeedszio.view

import org.scalatheagorist.freeflowfeedszio.models.Feed
import org.scalatheagorist.freeflowfeedszio.publisher.Props
import org.scalatheagorist.freeflowfeedszio.publisher.Props.Lang
import org.scalatheagorist.freeflowfeedszio.publisher.Props.Publisher
import zio.ULayer
import zio.ZLayer
import zio.stream.ZStream

trait FeedHtmlBuilder:
  def build[R, E](props: Option[Props])(
      stream: ZStream[R, E, Feed]
  ): ZStream[R, E, String]

object FeedHtmlBuilder:
  val layer: ULayer[FeedHtmlBuilder] =
    ZLayer.succeed {
      new FeedHtmlBuilder:
        def build[R, E](props: Option[Props])(
            stream: ZStream[R, E, Feed]
        ): ZStream[R, E, String] =
          (props match
            case None               => stream
            case Some(p: Publisher) => stream.filter(_.publisher == p)
            case Some(l: Lang)      => stream.filter(_.lang == l)
          ).flatMap(feed => ZStream.succeed(generateCards(feed)))
    }

  private def generateCards(feed: Feed): String =
    s"""
       |<div class="article-card">
       |    <div class="card mb-3 bg-primary text-white">
       |        <div class="card-body" onclick="window.open('${feed.article.link}', '_blank');" style="cursor: pointer;">
       |          ${s"""<p>${feed.author}</p>\n"""}
       |          ${s"""<p><span class="highlight-title">${feed.article.title}</span></p>"""}
       |          ${s"""<p><strong>Link:</strong><br><a href="${feed.article.link}" target="_blank">${feed.article.link}</a></p>"""}
       |        </div>
       |    </div>
       |</div>
       |""".stripMargin
