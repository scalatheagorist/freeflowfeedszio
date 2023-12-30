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
import zio.*
import zio.http.*

import java.time.Clock
import java.util.concurrent.TimeUnit

object AppServer extends ZIOAppDefault:
  private val server: ZIO[Configuration & RSSService, Throwable, Option[Nothing]] =
    ZIO
      .serviceWithZIO[Routes](routes =>
        Server
          .serve(routes.apply.withDefaultErrorResponse)
          .timeout(Duration(Int.MaxValue, TimeUnit.SECONDS))
          .provide(Server.defaultWithPort(8989))
      )
      .provideLayer(Routes.layer)

  private val zioHttpClient = ZLayer.suspend(Client.default)

  private val clock = ZLayer.succeed(Clock.systemUTC())

  private val databaseClientLive =
    (Configuration.live >>> DatabaseConnectionService.databaseLive) >>> DatabaseClient.layer

  private val htmlScrapeServiceLive =
    (clock ++ Configuration.live ++ (zioHttpClient >>> HttpClient.live) ++ databaseClientLive) >>> HtmlScrapeService.layer

  private val rssServiceLive =
    (clock ++ Configuration.live ++ databaseClientLive ++ RSSBuilder.layer ++ htmlScrapeServiceLive) >>> RSSService.layer

  // start server
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    ZIO
      .serviceWithZIO[Configuration] { configuration =>
        (for
          _ <- ZIO.logInfo(configuration.show)

          _ <- ZIO.serviceWithZIO[RSSService](_.runScraper.provideLayer(zioHttpClient).forkDaemon)

          _ <- ZIO.logInfo(s"Server started @ http://0.0.0.0:8989")
          _ <- server
        yield ()).provideLayer(Configuration.live ++ rssServiceLive)
      }
      .provideLayer(Configuration.live)
