package org.scalatheagorist.freeflowfeedszio.core.jdbc

import org.scalatheagorist.freeflowfeedszio.AppConfig
import zio.ZIO
import zio.ZLayer

import java.sql.Connection
import java.sql.DriverManager

object DatabaseConnectionService {
  private case class Credentials private (url: String, username: String, password: String)
  private object Credentials {
    def from(conf: AppConfig): Credentials =
      Credentials(conf.databaseConfig.url, conf.databaseConfig.username, conf.databaseConfig.password)
  }

  val databaseLive: ZLayer[AppConfig, RuntimeException, Connection] = ZLayer.fromZIO {
    (for {
      config <- ZIO.service[AppConfig]
      cred   <- ZIO.succeed(Credentials.from(config))
      conn   <- ZIO.attemptBlocking(DriverManager.getConnection(cred.url, cred.username, cred.password))
      _      <- if (conn.isClosed) ZIO.logInfo("datasource connection failed") else ZIO.logInfo("datasource created successfully")
    } yield conn).catchAll(err => ZIO.fail(new RuntimeException(s"Error creating data source: ${err.getMessage}")))
  }
}
