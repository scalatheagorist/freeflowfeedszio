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
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    val server: ZIO[RSSService, Throwable, Option[Nothing]] =
      ZIO.serviceWithZIO[Routes](routes =>
        Server
          .serve(routes.apply.withDefaultErrorResponse)
          .timeout(Duration(Int.MaxValue, TimeUnit.SECONDS))
          .provide(Server.defaultWithPort(8989))
      ).provideLayer(Routes.layer)

    val zioHttpClient = ZLayer.suspend(Client.default)

    val clock = ZLayer.succeed(Clock.systemUTC())

    val databaseClientLive =
      (AppConfig.live >>> DatabaseConnectionService.databaseLive) >>> DatabaseClient.layer

    val htmlScrapeServiceLive =
      (clock ++ AppConfig.live ++ (zioHttpClient >>> HttpClient.live) ++ databaseClientLive) >>> HtmlScrapeService.layer

    val rssServiceLive =
      (clock ++ AppConfig.live ++ databaseClientLive ++ RSSBuilder.layer ++ htmlScrapeServiceLive) >>> RSSService.layer

    // app start
    ZIO.serviceWithZIO[AppConfig] { conf =>
      (for {
        _ <- ZIO.logInfo(conf.show)

        _ <- ZIO.serviceWithZIO[RSSService](_.runScraper.provideLayer(zioHttpClient).forkDaemon)

        _ <- ZIO.logInfo(s"Server started @ http://0.0.0.0:8989")
        _ <- server
      } yield ()).provideLayer(rssServiceLive)
    }
  }.provideLayer(AppConfig.live)
}
