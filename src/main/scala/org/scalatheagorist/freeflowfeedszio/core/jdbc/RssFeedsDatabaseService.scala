package org.scalatheagorist.freeflowfeedszio.core.jdbc

import org.scalatheagorist.freeflowfeedszio.core.jdbc.models.RssFeeds
import org.scalatheagorist.freeflowfeedszio.publisher.Props
import zio.*
import zio.http.Client
import zio.stream.ZStream

import java.sql.Connection

trait RssFeedsDatabaseService extends SQLFunctions:
  def select(
    page: Int,
    pageSize: Int,
    props: Option[Props],
    searchTerm: Option[String]
  ): ZStream[Any, Throwable, RssFeeds]

  def insert(stream: ZStream[Client, Throwable, RssFeeds]): ZStream[Client, Throwable, Unit]

object RssFeedsDatabaseService:
  val layer: ZLayer[Connection, Nothing, RssFeedsDatabaseService] =
    ZLayer {
      ZIO.serviceWith[Connection] { conn =>
        given connection: Connection = conn

        new RssFeedsDatabaseService {
          override def select(
            page: Int,
            pageSize: Int,
            props: Option[Props],
            searchTerm: Option[String]
          ): ZStream[Any, Throwable, RssFeeds] =
            ZStream.fromIteratorZIO(
              for
                rs       <- select(RssFeeds.selectQuery(props, searchTerm, pageSize, page * pageSize))
                iterator <- ZIO.attempt {
                              new Iterator[RssFeeds] {
                                override def hasNext: Boolean = rs.next()
                                override def next(): RssFeeds = RssFeeds.from(rs)
                              }
                            }
              yield iterator
            )

          override def insert(feeds: ZStream[Client, Throwable, RssFeeds]): ZStream[Client, Throwable, Unit] =
            feeds.chunks.flatMap { feeds =>
              ZStream.blocking {
                ZStream.fromZIO {
                  insertBatch(
                    query = RssFeeds.insertQuery,
                    effect = stmt => RssFeeds.insertBatch(stmt, feeds)
                  ).catchAll(err => ZIO.fail(new RuntimeException(s"Error inserting data: ${err.getMessage}")))
                }
              }
            }
        }
      }
    }
