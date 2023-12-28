package org.scalatheagorist.freeflowfeedszio.core.jdbc.models

import cats.implicits.toShow
import cats.kernel.Monoid
import org.scalatheagorist.freeflowfeedszio.publisher.Category
import org.scalatheagorist.freeflowfeedszio.publisher.Category.Lang
import org.scalatheagorist.freeflowfeedszio.publisher.Category.Publisher
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
  private val insertQuery: String =
    """
      |INSERT INTO rss_feeds (id, author, title, link, publisher, lang, created)
      |VALUES (?, ?, ?, ?, ?, ?, ?)
      |ON CONFLICT (id) DO NOTHING
      |""".stripMargin

  def insertQuery(rssFeeds: Chunk[RssFeeds])(using connection: Connection): ZIO[Any, Throwable, Unit] =
    (for
      _    <- ZIO.succeed(connection.setAutoCommit(false))
      stmt <- ZIO.attempt(connection.prepareStatement(insertQuery))
      _    <- insertBatch(stmt, rssFeeds)
      _    <- ZIO.attempt(stmt.executeBatch()) *> ZIO.attempt(connection.commit())
      _    <- ZIO.attempt(stmt.close())
    yield stmt)
      .tapError(error => ZIO.logError(error.getMessage))
      .tapError(_ => ZIO.succeed(connection.rollback()))
      .unit

  private inline def insertBatch(stmt: PreparedStatement, rssFeeds: Chunk[RssFeeds]): ZIO[Any, Nothing, Chunk[Unit]] =
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

  private inline def whereClause(category: Option[Category], searchTerm: Option[String]): String =
    val search = searchTerm.map(st => s" author LIKE '%$st%' OR title LIKE '%$st%' OR link LIKE '%$st%' ")
    val clauseCategory = category match
      case Some(p: Publisher) => Some(s"publisher = '${p.show}'")
      case Some(l: Lang)      => Some(s"lang = '${l.show}'")
      case None               => None

    clauseCategory match
      case Some(enumeration) => s" WHERE $enumeration " ++ search.map(s => s" AND $s ").mkString
      case None              => search.map(s => s" WHERE $s ").mkString

  def selectQuery(category: Option[Category], searchTerm: Option[String], pageSize: Int, from: Int)(using
    conn: Connection
  ): ZIO[Any, Throwable, ResultSet] =
    val pagination: String = s" ORDER BY created DESC LIMIT $pageSize OFFSET $from"
    val fragment: String   = select ++ whereClause(category, searchTerm) ++ pagination

    ZIO.logInfo(fragment) *>
      ZIO
        .attempt(conn.prepareStatement(fragment))
        .map(_.executeQuery())
        .tapError(err => ZIO.logError(err.getMessage))
