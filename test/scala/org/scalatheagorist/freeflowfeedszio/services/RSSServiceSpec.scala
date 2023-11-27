package org.scalatheagorist.freeflowfeedszio.services

import org.mockito.ArgumentMatchersSugar.*
import org.mockito.IdiomaticMockito.StubbingOps
import org.mockito.MockitoSugar.mock
import org.scalatheagorist.freeflowfeedszio.AppConfig
import org.scalatheagorist.freeflowfeedszio.core.fs.FileStoreClient
import org.scalatheagorist.freeflowfeedszio.core.http.HttpClient
import org.scalatheagorist.freeflowfeedszio.publisher.Publisher
import org.scalatheagorist.freeflowfeedszio.view.RSSBuilder
import zio.Chunk
import zio.Scope
import zio.ZIO
import zio.ZLayer
import zio.durationInt
import zio.http.Client
import zio.stream.ZStream
import zio.test.Spec
import zio.test.TestEnvironment
import zio.test.ZIOSpecDefault
import zio.test.assertTrue

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

object RSSServiceSpec extends ZIOSpecDefault {
  private val appConfig = mock[AppConfig]
  private val fileStoreClient = mock[FileStoreClient]
  private val rssBuilder = mock[RSSBuilder]
  private val httpClient = mock[HttpClient]
  private val htmlScrapeService = mock[HtmlScrapeService]
  private val clock: Clock =
    Clock.fixed(
      Instant.parse("2023-11-15T12:00:00.00Z"),
      ZoneId.of("UTC")
    )

  private val clockLayer = ZLayer(ZIO.succeed(clock))
  private val appConfigLayer = ZLayer(ZIO.succeed(appConfig))
  private val rssBuilderLayer = ZLayer.succeed(rssBuilder)

  private val fileStoreConfigLayer =
    appConfigLayer >>> ZLayer(ZIO.service[AppConfig].map(_.fileStoreConfig))

  private val clientLayer =
    ZLayer.suspend(Client.default)

  private val httpClientLayer =
    clientLayer >>> ZLayer.succeed(httpClient)

  private val fileStoreClientLayer =
    (appConfigLayer ++ fileStoreConfigLayer) >>> ZLayer.succeed(fileStoreClient)

  private val htmlScrapeServiceLayer =
    (httpClientLayer ++ appConfigLayer ++ fileStoreClientLayer) >>> ZLayer.succeed(htmlScrapeService)


  override def spec = suite("RSSService")(
    suite("generateFeeds") {
      test("must work") {
        val expectedJson =
          """{"author": "Schweizer Monat", "article": {"title": "\u00abMan findet die Macht  nie dort, wo man sie sucht\u00bb", "link": "https://schweizermonat.ch/man-findet-die-macht-nie-dort-wo-man-sie-sucht/"}, "publisher": "SCHWEIZER_MONAT", "lang": "DE"}""".stripMargin

        // mock
        fileStoreClient.loadFromDir returns ZStream(Chunk.fromArray(expectedJson.getBytes("UTF-8")))
        rssBuilder.build(Some(Publisher.SCHWEIZERMONAT), None)(*) returns ZStream("<div>test</div>")

        // act & assert
        ZIO
          .serviceWithZIO[RSSService](_.generateFeeds(Some(Publisher.SCHWEIZERMONAT), None).runLast)
          .map(charSeq => assertTrue(charSeq.contains("<div>test</div>")))
          .provideLayer((
            clockLayer ++
              rssBuilderLayer ++
              appConfigLayer ++
              fileStoreClientLayer ++
              htmlScrapeServiceLayer
            ) >>> RSSService.layer)
      }
    },
    suite("runScraper") {
      test("must work") {
        htmlScrapeService.stream returns ZStream.unit.provideLayer(clientLayer)

        ZIO.serviceWithZIO[RSSService] { service =>
          (for {
            fiber     <- service.runScraper.forkDaemon
            isRunning <- fiber.status.map(_.isRunning)
          } yield assertTrue(isRunning)).provideLayer(clientLayer)
        }.provideLayer((
          clockLayer ++
            rssBuilderLayer ++
            fileStoreClientLayer ++
            htmlScrapeServiceLayer ++
            appConfigLayer
          ) >>> RSSService.layer)
      }
    })
}
