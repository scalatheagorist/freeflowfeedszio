package org.scalatheagorist.freeflowfeedszio.models

import cats.implicits.toShow
import org.scalatheagorist.freeflowfeedszio.core.jdbc.models.RssFeeds
import org.scalatheagorist.freeflowfeedszio.publisher.Lang
import org.scalatheagorist.freeflowfeedszio.publisher.Publisher

import java.time.Clock
import java.time.LocalDateTime

final case class RSSFeed(author: String, article: Article, publisher: Publisher, lang: Lang)

object RSSFeed {
  def from(rssFeeds: RssFeeds): RSSFeed =
    RSSFeed(
      author  = rssFeeds.author,
      article = Article(
        title = rssFeeds.title,
        link  = rssFeeds.link
      ),
      publisher = Publisher.from(rssFeeds.publisher),
      lang      = Lang.from(rssFeeds.lang),
    )

  implicit class RichRSSFeed(rssFeed: RSSFeed) {
    def toDbRssFeeds(clock: Clock): RssFeeds =
      RssFeeds(
        id        = Math.abs(rssFeed.hashCode().toLong),
        author    = rssFeed.author,
        title     = rssFeed.article.title,
        link      = rssFeed.article.link,
        publisher = rssFeed.publisher.show,
        lang      = rssFeed.lang.show,
        created   = LocalDateTime.now(clock)
      )
  }
}
