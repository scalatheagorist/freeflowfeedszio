package org.scalatheagorist.freeflowfeedszio.core.jdbc

import org.scalatheagorist.freeflowfeedszio.core.jdbc.models.RssFeeds
import zio.Chunk
import zio.ZIO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

trait SQLFunctions:
  def insertBatch[B](query: String, effect: PreparedStatement => ZIO[Any, Throwable, B])(using
    connection: Connection
  ): ZIO[Any, Throwable, Unit] =
    (for
      _    <- ZIO.succeed(connection.setAutoCommit(false))
      stmt <- ZIO.attempt(connection.prepareStatement(query))
      _    <- effect(stmt)
      _    <- ZIO.attempt(stmt.executeBatch()) *> ZIO.attempt(connection.commit())
      _    <- ZIO.attempt(stmt.close())
    yield stmt)
      .tapError(error => ZIO.logError(error.getMessage))
      .tapError(_ => ZIO.succeed(connection.rollback()))
      .unit

  def select(query: String)(using connection: Connection): ZIO[Any, Throwable, ResultSet] =
    ZIO
      .attempt(connection.prepareStatement(query))
      .map(_.executeQuery())
      .tapError(err => ZIO.logError(err.getMessage))
