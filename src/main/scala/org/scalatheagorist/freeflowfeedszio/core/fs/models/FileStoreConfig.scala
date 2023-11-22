package org.scalatheagorist.freeflowfeedszio.core.fs.models

import cats.Show

final case class FileStoreConfig(concurrency: Int, path: String, suffix: String)

object FileStoreConfig {
  implicit val show: Show[FileStoreConfig] = config =>
    s"""
       |    concurrency:${config.concurrency}
       |    dir:${config.path}
       |    suffix:${config.suffix}""".stripMargin
}
