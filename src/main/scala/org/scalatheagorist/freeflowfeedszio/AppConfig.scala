package org.scalatheagorist.freeflowfeedszio

import cats.Show
import cats.implicits.showInterpolator
import org.scalatheagorist.freeflowfeedszio.core.fs.models.FileStoreConfig
import org.scalatheagorist.freeflowfeedszio.publisher.Hosts
import org.scalatheagorist.freeflowfeedszio.publisher.PublisherHost
import zio.IO
import zio.config._
import zio.config.magnolia._
import zio.config.typesafe.TypesafeConfigSource

import java.io.File

final case class AppConfig(
    hosts: List[PublisherHost],
    fileStoreConfig: FileStoreConfig,
    scrapeConcurrency: Int,
    update: String,
    updateInterval: Int,
    // initial load of first scraped elements
    initialReverse: Boolean
)

object AppConfig {
  implicit val show: Show[AppConfig] = appConfig =>
    show"""
       |hosts:           ${Hosts.show.show(appConfig.hosts)}
       |fileStoreConfig: ${appConfig.fileStoreConfig}
       |concurrency:     ${appConfig.scrapeConcurrency}
       |update:          ${appConfig.update}
       |updateInterval:  ${appConfig.updateInterval}
       |initialReverse:  ${appConfig.initialReverse}
       |""".stripMargin

  def from(file: File): IO[ReadError[String], AppConfig] =
    read(descriptor[AppConfig] from TypesafeConfigSource.fromHoconFile(file))
}
