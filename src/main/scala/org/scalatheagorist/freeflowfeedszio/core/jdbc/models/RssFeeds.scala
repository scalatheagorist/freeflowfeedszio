package org.scalatheagorist.freeflowfeedszio.core.jdbc.models

import cats.implicits.toShow
import cats.kernel.Monoid
import org.scalatheagorist.freeflowfeedszio.publisher.Props
import org.scalatheagorist.freeflowfeedszio.publisher.Props.Lang
import org.scalatheagorist.freeflowfeedszio.publisher.Props.Publisher
import zio.*

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.LocalDateTime

final case class RssFeeds(
  id: Long,
  author: String,
  title: String,
  link: String,
  publisher: String,
  lang: String,
  created: LocalDateTime
)

object RssFeeds:
  val insertQuery: String =
    """
      |INSERT INTO rss_feeds (id, author, title, link, publisher, lang, created)
      |VALUES (?, ?, ?, ?, ?, ?, ?)
      |ON CONFLICT (id) DO NOTHING
      |""".stripMargin

  def insertBatch(stmt: PreparedStatement, rssFeeds: Chunk[RssFeeds]): ZIO[Any, Nothing, Chunk[Unit]] =
    ZIO.succeed {
      rssFeeds.map { rss =>
        stmt.setLong(1, rss.id)
        stmt.setString(2, rss.author)
        stmt.setString(3, rss.title)
        stmt.setString(4, rss.link)
        stmt.setString(5, rss.publisher.show)
        stmt.setString(6, rss.lang.show)
        stmt.setTimestamp(7, Timestamp.valueOf(rss.created))
        stmt.addBatch()
      }
    }

  def from(resultSet: ResultSet): RssFeeds =
    RssFeeds(
      id = resultSet.getLong("id"),
      author = resultSet.getString("author"),
      title = resultSet.getString("title"),
      link = resultSet.getString("link"),
      publisher = resultSet.getString("publisher"),
      lang = resultSet.getString("lang"),
      created = resultSet.getTimestamp("created").toLocalDateTime
    )

  private val select: String = "SELECT id, author, title, link, publisher, lang, created FROM rss_feeds"

  private inline def whereClause(props: Option[Props], searchTerm: Option[String]): String =
    val search      = searchTerm.map(st => s" author LIKE '%$st%' OR title LIKE '%$st%' OR link LIKE '%$st%' ")
    val clauseProps = props match
      case Some(p: Publisher) => Some(s"publisher = '${p.show}'")
      case Some(l: Lang)      => Some(s"lang = '${l.show}'")
      case None               => None

    clauseProps match
      case Some(props) => s" WHERE $props " ++ search.map(s => s" AND $s ").mkString
      case None        => search.map(s => s" WHERE $s ").mkString

  def selectQuery(props: Option[Props], searchTerm: Option[String], pageSize: Int, from: Int): String =
    select ++ whereClause(props, searchTerm) ++ s" ORDER BY created DESC LIMIT $pageSize OFFSET $from"
