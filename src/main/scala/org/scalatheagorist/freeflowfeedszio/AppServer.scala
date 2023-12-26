package org.scalatheagorist.freeflowfeedszio

import cats.implicits.toShow
import org.scalatheagorist.freeflowfeedszio.core.http.HttpClient
import org.scalatheagorist.freeflowfeedszio.core.jdbc.DatabaseClient
import org.scalatheagorist.freeflowfeedszio.core.jdbc.DatabaseConnectionService
import org.scalatheagorist.freeflowfeedszio.services.HtmlScrapeService
import org.scalatheagorist.freeflowfeedszio.services.RSSService
import org.scalatheagorist.freeflowfeedszio.view.RSSBuilder
import zio.Duration
import zio.ZIOAppDefault
import zio._
import zio.http._

import java.time.Clock
import java.util.concurrent.TimeUnit

object AppServer extends ZIOAppDefault {
  private val server: ZIO[AppConfig & RSSService, Throwable, Option[Nothing]] =
    ZIO.serviceWithZIO[Routes](routes =>
      Server
        .serve(routes.apply.withDefaultErrorResponse)
        .timeout(Duration(Int.MaxValue, TimeUnit.SECONDS))
        .provide(Server.defaultWithPort(8989))
    ).provideLayer(Routes.layer)

  private val zioHttpClient =
    ZLayer.suspend(Client.default)

  private val clock =
    ZLayer.succeed(Clock.systemUTC())

  private val databaseClientLive =
    (AppConfig.live >>> DatabaseConnectionService.databaseLive) >>> DatabaseClient.layer

  private val htmlScrapeServiceLive =
    (clock ++ AppConfig.live ++ (zioHttpClient >>> HttpClient.live) ++ databaseClientLive) >>> HtmlScrapeService.layer

  private val rssServiceLive =
    (clock ++ AppConfig.live ++ databaseClientLive ++ RSSBuilder.layer ++ htmlScrapeServiceLive) >>> RSSService.layer

  // start server
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    ZIO.serviceWithZIO[AppConfig] { conf =>
      (for {
        _ <- ZIO.logInfo(conf.show)

        _ <- ZIO.serviceWithZIO[RSSService](_.runScraper.provideLayer(zioHttpClient).forkDaemon)

        _ <- ZIO.logInfo(s"Server started @ http://0.0.0.0:8989")
        _ <- server
      } yield ()).provideLayer(AppConfig.live ++ rssServiceLive)
    }.provideLayer(AppConfig.live)
}
