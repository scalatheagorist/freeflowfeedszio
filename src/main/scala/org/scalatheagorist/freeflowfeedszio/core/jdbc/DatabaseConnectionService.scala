package org.scalatheagorist.freeflowfeedszio.core.jdbc

import org.scalatheagorist.freeflowfeedszio.Configuration
import zio.ZIO
import zio.ZLayer

import java.sql.Connection
import java.sql.DriverManager

object DatabaseConnectionService:
    private case class Credentials private (url: String, username: String, password: String)

    private object Credentials:
        def from(conf: Configuration): Credentials =
            Credentials(conf.databaseConfig.url, conf.databaseConfig.username, conf.databaseConfig.password)

    private val statusSkeleton = "datasource connection"

    val databaseLive: ZLayer[Configuration, RuntimeException, Connection] = ZLayer.fromZIO {
        (for {
            config <- ZIO.service[Configuration]
            cred   <- ZIO.succeed(Credentials.from(config))
            conn   <- ZIO.attemptBlocking(DriverManager.getConnection(cred.url, cred.username, cred.password))
            _      <- if conn.isClosed then ZIO.logInfo(s"$statusSkeleton failed")
                      else ZIO.logInfo(s"$statusSkeleton successfully")
        } yield conn).catchAll(err => ZIO.fail(new RuntimeException(s"Error creating data source: ${err.getMessage}")))
    }
