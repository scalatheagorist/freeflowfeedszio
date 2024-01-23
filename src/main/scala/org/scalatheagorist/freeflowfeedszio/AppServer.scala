package org.scalatheagorist.freeflowfeedszio

import caliban.ZHttpAdapter
import caliban.interop.tapir.HttpInterpreter
import cats.implicits.toShow
import org.scalatheagorist.freeflowfeedszio.core.http.HttpClient
import org.scalatheagorist.freeflowfeedszio.core.jdbc.FeedsDatabaseService
import org.scalatheagorist.freeflowfeedszio.core.jdbc.DatabaseConnectionService
import org.scalatheagorist.freeflowfeedszio.services.HtmlScrapeService
import org.scalatheagorist.freeflowfeedszio.services.FeedService
import org.scalatheagorist.freeflowfeedszio.view.FeedHtmlBuilder
import zio.Duration
import zio.ZIOAppDefault
import zio.*
import zio.http.*
import caliban.{GraphQL, ZHttpAdapter}
import caliban.interop.tapir.{HttpInterpreter, WebSocketInterpreter}
import java.time.Clock
import java.util.concurrent.TimeUnit

object AppServer extends ZIOAppDefault:
  import sttp.tapir.json.circe.*

  private val server: ZIO[Configuration & FeedService, Throwable, Option[Nothing]] =
    for
      interpreter <- ZIO.serviceWithZIO[AppRoutes](_.apply).provideLayer(AppRoutes.layer)
      server      <- Server
                       .serve(
                         Routes(
                           Method.ANY / "api" / "graphql" ->
                             ZHttpAdapter.makeHttpService(HttpInterpreter(interpreter))
                         ).toHttpApp
                       )
                       .timeout(Duration(Int.MaxValue, TimeUnit.SECONDS))
                       .provide(Server.defaultWithPort(8989))
    yield server

  private val zioHttpClient = ZLayer.suspend(Client.default ++ Scope.default)

  private val clock = ZLayer.succeed(Clock.systemUTC())

  private val databaseClientLive =
    (Configuration.live >>> DatabaseConnectionService.databaseLive) >>> FeedsDatabaseService.layer

  private val htmlScrapeServiceLive =
    (clock ++ Configuration.live ++ (zioHttpClient >>> HttpClient.live) ++ databaseClientLive) >>> HtmlScrapeService.layer

  private val feedServiceLive =
    (clock ++ Configuration.live ++ databaseClientLive ++ FeedHtmlBuilder.layer ++ htmlScrapeServiceLive) >>> FeedService.layer

  // start server
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    ZIO
      .serviceWithZIO[Configuration] { configuration =>
        (for
          _ <- ZIO.logInfo(configuration.show)

          _ <- ZIO.serviceWithZIO[FeedService](_.runScraper.provideLayer(zioHttpClient).forkDaemon)

          _ <- ZIO.logInfo(s"Server started @ http://0.0.0.0:8989")
          _ <- server
        yield ()).provideLayer(Configuration.live ++ feedServiceLive)
      }
      .provideLayer(Configuration.live)
