package org.scalatheagorist.freeflowfeedszio.core.jdbc

import org.scalatheagorist.freeflowfeedszio.core.jdbc.models.FeedRow
import org.scalatheagorist.freeflowfeedszio.publisher.Props
import zio.*
import zio.http.Client
import zio.stream.ZStream

import java.sql.Connection

trait FeedsDatabaseService extends SQLFunctions:
  def select(
      page: Int,
      pageSize: Int,
      props: Option[Props],
      searchTerm: Option[String]
  ): ZStream[Any, Throwable, FeedRow]

  def insert(stream: ZStream[Client & Scope, Throwable, FeedRow]): ZStream[Client & Scope, Throwable, Unit]

object FeedsDatabaseService:
  val layer: ZLayer[Connection, Nothing, FeedsDatabaseService] =
    ZLayer {
      ZIO.serviceWith[Connection] { conn =>
        given connection: Connection = conn

        new FeedsDatabaseService {
          override def select(
              page: Int,
              pageSize: Int,
              props: Option[Props],
              searchTerm: Option[String]
          ): ZStream[Any, Throwable, FeedRow] =
            ZStream.fromIteratorZIO(
              for
                rs       <- select(FeedRow.selectQuery(props, searchTerm, pageSize, page * pageSize))
                iterator <- ZIO.attempt {
                              new Iterator[FeedRow] {
                                override def hasNext: Boolean = rs.next()
                                override def next(): FeedRow  = FeedRow.from(rs)
                              }
                            }
              yield iterator
            )

          override def insert(
              feeds: ZStream[Client & Scope, Throwable, FeedRow]
          ): ZStream[Client & Scope, Throwable, Unit] =
            feeds.chunks.flatMap { feeds =>
              ZStream.blocking {
                ZStream.fromZIO {
                  insertBatch(
                    query = FeedRow.insertQuery,
                    effect = stmt => FeedRow.insertBatch(stmt, feeds)
                  ).catchAll(err => ZIO.fail(new RuntimeException(s"Error inserting data: ${err.getMessage}")))
                }
              }
            }
        }
      }
    }
