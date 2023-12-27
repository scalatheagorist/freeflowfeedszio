package org.scalatheagorist.freeflowfeedszio.core.jdbc

import org.scalatheagorist.freeflowfeedszio.core.jdbc.models.RssFeeds
import org.scalatheagorist.freeflowfeedszio.publisher.Lang
import org.scalatheagorist.freeflowfeedszio.publisher.Publisher
import zio._
import zio.http.Client
import zio.stream.ZStream

import java.sql.Connection

trait DatabaseClient {
  def select(
    page: Int,
    pageSize: Int,
    publisher: Option[Publisher],
    lang: Option[Lang],
    searchTerm: Option[String]
  ): ZStream[Any, Throwable, RssFeeds]

  def insert(stream: ZStream[Client, Throwable, RssFeeds]): ZStream[Client, Throwable, Unit]
}

object DatabaseClient {
  val layer: ZLayer[Connection, Nothing, DatabaseClient] =
    ZLayer {
      ZIO.serviceWith[Connection] { implicit conn =>
        new DatabaseClient {
          override def select(
            page: Int,
            pageSize: Int,
            publisher: Option[Publisher],
            lang: Option[Lang],
            searchTerm: Option[String]
          ): ZStream[Any, Throwable, RssFeeds] =
            ZStream.fromIteratorZIO(
              for {
                rs       <- RssFeeds.selectQuery(publisher, lang, searchTerm, pageSize, page * pageSize)
                iterator <- ZIO.attempt {
                              new Iterator[RssFeeds] {
                                override def hasNext: Boolean = rs.next()
                                override def next(): RssFeeds = RssFeeds.from(rs)
                              }
                            }
              } yield iterator
            )

          override def insert(feeds: ZStream[Client, Throwable, RssFeeds]): ZStream[Client, Throwable, Unit] =
            feeds.chunks.flatMap { feeds =>
              ZStream.blocking {
                ZStream.fromZIO {
                  RssFeeds
                    .insertQuery(feeds)
                    .catchAll(err => ZIO.fail(new RuntimeException(s"Error inserting data: ${err.getMessage}")))
                }
              }
            }
        }
      }
    }
}
