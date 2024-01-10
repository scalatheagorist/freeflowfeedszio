package org.scalatheagorist.freeflowfeedszio.core.jdbc.models

import cats.Show

final case class DatabaseConfig(url: String, username: String, password: String)

object DatabaseConfig:
    given show: Show[DatabaseConfig] with
        def show(config: DatabaseConfig): String =
            s"""
               |    url:${config.url}
               |    username:${config.username}""".stripMargin
