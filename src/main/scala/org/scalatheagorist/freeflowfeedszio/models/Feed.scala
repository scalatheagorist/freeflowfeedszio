package org.scalatheagorist.freeflowfeedszio.models

import cats.Show
import cats.implicits.toShow
import org.scalatheagorist.freeflowfeedszio.core.jdbc.models.FeedRow
import org.scalatheagorist.freeflowfeedszio.publisher.Props.*
import org.scalatheagorist.freeflowfeedszio.publisher.Props.Publisher.*
import org.scalatheagorist.freeflowfeedszio.publisher.PublisherUrl
import org.scalatheagorist.freeflowfeedszio.publisher.PublisherUrl.*
import org.scalatheagorist.freeflowfeedszio.publisher.models.*
import org.scalatheagorist.freeflowfeedszio.util.RichURL.*
import zio.stream.ZStream

import java.time.Clock
import java.time.LocalDateTime

final case class Feed(author: String, article: Article, publisher: Publisher, lang: Lang)

object Feed:
    def from(feed: FeedRow): Feed =
        Feed(
          author = feed.author,
          article = Article(
            title = feed.title,
            link = feed.link
          ),
          publisher = Publisher.from(feed.publisher),
          lang = Lang.from(feed.lang)
        )

    def from(htmlResponse: HtmlResponse, url: PublisherUrl): ZStream[Any, Throwable, Feed] =
        htmlResponse.publisher match
            case EFMAGAZIN       => EfMagazin(url.url.toProtocolWithHost).toFeedStream(htmlResponse)
            case FREIHEITSFUNKEN => Freiheitsfunken(url.url.toProtocolWithHost).toFeedStream(htmlResponse)
            case MISESDE         => MisesDE.toFeedStream(htmlResponse)
            case SCHWEIZER_MONAT => SchweizerMonat.toFeedStream(htmlResponse)

    extension (feed: Feed)
        def toFeedRow(clock: Clock): FeedRow =
            FeedRow(
              id = Math.abs(feed.hashCode().toLong),
              author = feed.author,
              title = feed.article.title,
              link = feed.article.link,
              publisher = feed.publisher.show,
              lang = feed.lang.show,
              created = LocalDateTime.now(clock)
            )
