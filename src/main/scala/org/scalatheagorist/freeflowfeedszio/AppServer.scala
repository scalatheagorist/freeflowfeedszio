package org.scalatheagorist.freeflowfeedszio

import org.scalatheagorist.freeflowfeedszio.core.fs.FileStoreClient
import org.scalatheagorist.freeflowfeedszio.core.fs.models.FileStoreConfig
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
  private val clientLive: ZLayer[Any, Throwable, Client] = ZLayer.suspend(Client.default)

  private val clockLive: ULayer[Clock] = ZLayer.succeed(Clock.systemUTC())

  private val appConfigLive: ZLayer[Any, Throwable, AppConfig] = {
    ZLayer.fromZIO {
      for {
        baseDir <- ZIO.attempt(new File(java.lang.System.getProperty("user.dir")))
        _       <- ZIO.logInfo(s"baseDir: ${baseDir.getAbsolutePath}")

        confDir <- ZIO.attempt(new File(baseDir, "src/main/resources/application.conf"))
        _       <- ZIO.logInfo(s"conf dir: ${confDir.getAbsolutePath}")

        layer   <- AppConfig.from(confDir)
      } yield layer
    }
  }

  override def run: ZIO[Any with ZIOAppArgs with Scope, Nothing, ExitCode] =
    (for {
      _          <- ZIO.serviceWith[RSSService](_.runScraper.forkDaemon)

      _          <- ZIO.logInfo(s"Server started @ http://0.0.0.0:8989")
      _          <- server
    } yield ())
      .provideLayer(
        ((appConfigLive ++ FileStoreConfig.live) >>> FileStoreClient.layer) ++
        ((clientLive ++ appConfigLive ++ FileStoreConfig.live ++ FileStoreClient.layer) >>> HtmlScrapeService.layer) ++
        ((clockLive ++ RSSBuilder.layer ++ appConfigLive ++ FileStoreClient.layer ++ HtmlScrapeService.layer) >>> RSSService.layer)
      )
      .exitCode

  private def server: ZIO[RSSService, Throwable, Option[Nothing]] =
    ZIO.serviceWithZIO[Routes](routes =>
      Server
        .serve(routes.apply.withDefaultErrorResponse)
        .timeout(Duration(600L, TimeUnit.SECONDS))
        .provide(Server.defaultWithPort(8989))
    ).provideLayer(Routes.live)
}
