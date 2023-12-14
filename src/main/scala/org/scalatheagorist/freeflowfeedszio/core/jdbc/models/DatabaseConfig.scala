package org.scalatheagorist.freeflowfeedszio.core.jdbc.models

import cats.Show

final case class DatabaseConfig(url: String, username: String, password: String)

object DatabaseConfig {
  implicit val show: Show[DatabaseConfig] = config =>
    s"""
       |    url:${config.url}
       |    username:${config.username}
       """.stripMargin
}
