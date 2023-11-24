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
import zio.http.Client
import zio.stream.ZStream
import zio.test.Spec
import zio.test.TestEnvironment
import zio.test.ZIOSpecDefault
import zio.test.assertTrue

object RSSServiceSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment with Scope, Any] = suite("RSSService") {
    test("generateFeeds") {
      val fileStoreClient = mock[FileStoreClient]
      val rssBuilder = mock[RSSBuilder]
      val httpClient = mock[HttpClient]
      val htmlScrapeService = mock[HtmlScrapeService]

      val clockLayer = ZLayer(ZIO.succeed(mock[java.time.Clock]))
      val appConfigLayer = ZLayer(ZIO.succeed(mock[AppConfig]))

      val fileStoreConfigLayer =
        appConfigLayer >>> ZLayer(ZIO.service[AppConfig].map(_.fileStoreConfig))

      val clientLayer = ZLayer.suspend(Client.default)

      val httpClientLayer =
        clientLayer >>> ZLayer.succeed(httpClient)

      val fileStoreClientLayer =
        (appConfigLayer ++ fileStoreConfigLayer) >>> ZLayer.succeed(fileStoreClient)

      val htmlScrapeServiceLayer =
        (httpClientLayer ++ appConfigLayer ++ fileStoreClientLayer) >>> ZLayer.succeed(htmlScrapeService)

      val expectedJson = """{"author": "Schweizer Monat", "article": {"title": "\u00abMan findet die Macht  nie dort, wo man sie sucht\u00bb", "link": "https://schweizermonat.ch/man-findet-die-macht-nie-dort-wo-man-sie-sucht/"}, "publisher": "SCHWEIZER_MONAT", "lang": "DE"}""".stripMargin

      (for {
        _        <- ZIO.succeed(fileStoreClient.loadFromDir returns ZStream(Chunk.fromArray(expectedJson.getBytes("UTF-8"))))
        _        <- ZIO.succeed(rssBuilder.build(Some(Publisher.SCHWEIZERMONAT), None)(*) returns ZStream("<div>test</div>"))

        charSeq  <- ZIO.serviceWithZIO[RSSService] { service =>
                   service.generateFeeds(Some(Publisher.SCHWEIZERMONAT), None).runLast
                 }
      } yield assertTrue(charSeq.contains("<div>test</div>")))
        .provideLayer(
          (clockLayer ++
            ZLayer.succeed(rssBuilder) ++
            appConfigLayer ++
            fileStoreClientLayer ++
            htmlScrapeServiceLayer
          ) >>> RSSService.layer
        )
    }
  }
}
