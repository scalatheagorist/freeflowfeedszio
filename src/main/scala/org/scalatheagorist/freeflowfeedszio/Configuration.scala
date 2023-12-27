package org.scalatheagorist.freeflowfeedszio

import cats.Show
import cats.implicits.showInterpolator
import org.scalatheagorist.freeflowfeedszio.core.jdbc.models.DatabaseConfig
import org.scalatheagorist.freeflowfeedszio.publisher.Hosts
import org.scalatheagorist.freeflowfeedszio.publisher.PublisherHost
import zio.ZIO
import zio.ZLayer
import zio.config._
import zio.config.magnolia._
import zio.config.typesafe.TypesafeConfigSource

import java.io.File

final case class Configuration(
    hosts: List[PublisherHost],
    databaseConfig: DatabaseConfig,
    scrapeConcurrency: Int,
    update: String,
    updateInterval: Int,
    initialReverse: Boolean, // initial load for the first scraped elements
    pageSize: Int
)

object Configuration {
  implicit val show: Show[Configuration] = appConfig =>
    show"""
       |hosts:           ${Hosts.show.show(appConfig.hosts)}
       |databaseConfig:  ${appConfig.databaseConfig}
       |concurrency:     ${appConfig.scrapeConcurrency}
       |update:          ${appConfig.update}
       |updateInterval:  ${appConfig.updateInterval}
       |initialReverse:  ${appConfig.initialReverse}
       |pageSize:        ${appConfig.pageSize}
       |""".stripMargin

  val live: ZLayer[Any, Throwable, Configuration] =
    ZLayer.fromZIO {
      for {
        baseDir <- ZIO.attempt(new File(java.lang.System.getProperty("user.dir")))
        _       <- ZIO.logInfo(s"baseDir: ${baseDir.getAbsolutePath}")

        confDir <- ZIO.attempt(new File(baseDir, "src/main/resources/application.conf"))
        _       <- ZIO.logInfo(s"conf dir: ${confDir.getAbsolutePath}")

        config  <- read(descriptor[Configuration] from TypesafeConfigSource.fromHoconFile(confDir))
      } yield config
    }
}
