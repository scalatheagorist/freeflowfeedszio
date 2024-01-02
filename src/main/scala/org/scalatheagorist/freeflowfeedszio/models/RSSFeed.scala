package org.scalatheagorist.freeflowfeedszio.models

import cats.Show
import cats.implicits.toShow
import org.scalatheagorist.freeflowfeedszio.core.jdbc.models.RssFeeds
import org.scalatheagorist.freeflowfeedszio.publisher.Props.*
import org.scalatheagorist.freeflowfeedszio.publisher.Props.Publisher.*
import org.scalatheagorist.freeflowfeedszio.publisher.Hosts.PublisherUrl
import org.scalatheagorist.freeflowfeedszio.publisher.Hosts.PublisherUrl.*
import org.scalatheagorist.freeflowfeedszio.publisher.models.*
import org.scalatheagorist.freeflowfeedszio.util.RichURL.*
import zio.stream.ZStream

import java.time.Clock
import java.time.LocalDateTime

final case class RSSFeed(author: String, article: Article, publisher: Publisher, lang: Lang)

object RSSFeed:
  def from(rssFeeds: RssFeeds): RSSFeed =
    RSSFeed(
      author = rssFeeds.author,
      article = Article(
        title = rssFeeds.title,
        link = rssFeeds.link
      ),
      publisher = Publisher.from(rssFeeds.publisher),
      lang = Lang.from(rssFeeds.lang)
    )

  def from(htmlResponse: HtmlResponse, url: PublisherUrl): ZStream[Any, Throwable, RSSFeed] =
    htmlResponse.publisher match
      case EFMAGAZIN       => EfMagazin(url.url.toProtocolWithHost).toRSSFeedStream(htmlResponse)
      case FREIHEITSFUNKEN => Freiheitsfunken(url.url.toProtocolWithHost).toRSSFeedStream(htmlResponse)
      case MISESDE         => MisesDE.toRSSFeedStream(htmlResponse)
      case SCHWEIZER_MONAT => SchweizerMonat.toRSSFeedStream(htmlResponse)

  extension (rssFeed: RSSFeed)
    def toDatabaseRssFeeds(clock: Clock): RssFeeds =
      RssFeeds(
        id = Math.abs(rssFeed.hashCode().toLong),
        author = rssFeed.author,
        title = rssFeed.article.title,
        link = rssFeed.article.link,
        publisher = rssFeed.publisher.show,
        lang = rssFeed.lang.show,
        created = LocalDateTime.now(clock)
      )
