package org.scalatheagorist.freeflowfeedszio

import org.scalatheagorist.freeflowfeedszio.core.fs.FileStoreClient
import org.scalatheagorist.freeflowfeedszio.core.fs.models.FileStoreConfig
import org.scalatheagorist.freeflowfeedszio.core.http.HttpClient
import org.scalatheagorist.freeflowfeedszio.services.HtmlScrapeService
import org.scalatheagorist.freeflowfeedszio.services.RSSService
import org.scalatheagorist.freeflowfeedszio.view.RSSBuilder
import zio.Duration
import zio.ZIOAppDefault
import zio._
import zio.http._

import java.io.File
import java.time.Clock
import java.util.concurrent.TimeUnit

object AppServer extends ZIOAppDefault { self =>
  override def run: ZIO[Any with ZIOAppArgs with Scope, Nothing, ExitCode] =
    (for {
      _ <- ZIO.serviceWith[RSSService](_.runScraper.forkDaemon)

      _ <- ZIO.logInfo(s"Server started @ http://0.0.0.0:8989")
      _ <- server
    } yield ())
      .provideLayer(mainLayer)
      .exitCode

  private def server: ZIO[RSSService, Throwable, Option[Nothing]] =
    ZIO.serviceWithZIO[Routes](routes =>
      Server
        .serve(routes.apply.withDefaultErrorResponse)
        .timeout(Duration(600L, TimeUnit.SECONDS))
        .provide(Server.defaultWithPort(8989))
    ).provideLayer(Routes.live)

  private lazy val mainLayer =
    httpClientLayer >>> fileStoreClientLayer >>> htmlScrapeServiceLayer >>> rssServiceLayer

  private lazy val clientLayer = ZLayer.suspend(Client.default)

  private lazy val clockLayer = ZLayer.succeed(Clock.systemUTC())

  private lazy val appConfigLayer =
    ZLayer.fromZIO {
      for {
        baseDir <- ZIO.attempt(new File(java.lang.System.getProperty("user.dir")))
        _       <- ZIO.logInfo(s"baseDir: ${baseDir.getAbsolutePath}")

        confDir <- ZIO.attempt(new File(baseDir, "src/main/resources/application.conf"))
        _       <- ZIO.logInfo(s"conf dir: ${confDir.getAbsolutePath}")

        layer   <- AppConfig.from(confDir)
      } yield layer
    }

  private lazy val fileStoreConfigLayer =
    appConfigLayer >>> ZLayer(ZIO.service[AppConfig].map(_.fileStoreConfig))

  private lazy val httpClientLayer =
    clientLayer >>> HttpClient.layer

  private lazy val fileStoreClientLayer =
    (appConfigLayer ++ fileStoreConfigLayer) >>> FileStoreClient.layer

  private lazy val htmlScrapeServiceLayer =
    (httpClientLayer ++ appConfigLayer ++ fileStoreClientLayer) >>> HtmlScrapeService.layer

  private lazy val rssServiceLayer =
    (clockLayer ++ RSSBuilder.layer ++ appConfigLayer ++ fileStoreClientLayer) >>> RSSService.layer
}
