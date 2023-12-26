package org.scalatheagorist.freeflowfeedszio.services

import org.mockito.ArgumentMatchersSugar.*
import org.mockito.IdiomaticMockito.StubbingOps
import org.mockito.MockitoSugar.mock
import org.scalatheagorist.freeflowfeedszio.AppConfig
import org.scalatheagorist.freeflowfeedszio.publisher.Publisher
import org.scalatheagorist.freeflowfeedszio.view.RSSBuilder
import zio.Chunk
import zio.Scope
import zio.ZIO
import zio.stream.ZStream
import zio.test.Spec
import zio.test.TestEnvironment
import zio.test.ZIOSpecDefault
import zio.test.assertTrue

import java.time.Clock
/*
object RSSServiceSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment with Scope, Any] = suite("RSSService") {
    test("generateFeeds") {
      val appConfig = mock[AppConfig]
      val htmlScrapeService = mock[HtmlScrapeService]
      val fileStoreClient = mock[FileStoreClient]
      val rssBuilder = mock[RSSBuilder]
      implicit val clock: Clock = Clock.systemUTC()
      val expectedJson = """{"author": "Schweizer Monat", "article": {"title": "\u00abMan findet die Macht  nie dort, wo man sie sucht\u00bb", "link": "https://schweizermonat.ch/man-findet-die-macht-nie-dort-wo-man-sie-sucht/"}, "publisher": "SCHWEIZER_MONAT", "lang": "DE"}""".stripMargin

      for {
        _       <- ZIO.succeed(fileStoreClient.loadFromDir returns ZStream(Chunk.fromArray(expectedJson.getBytes("UTF-8"))))
        _       <- ZIO.succeed(rssBuilder.build(Some(Publisher.SCHWEIZERMONAT), None)(*) returns ZStream("<div>test</div>"))

        service <- ZIO.succeed(new RSSService(appConfig, htmlScrapeService, fileStoreClient, rssBuilder))
        charSeq <- service.getFeeds(Some(Publisher.SCHWEIZERMONAT), None).runLast
      } yield assertTrue(charSeq.contains("<div>test</div>"))
    }
  }
}
*/
