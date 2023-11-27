package org.scalatheagorist.freeflowfeedszio.services

import org.mockito.ArgumentMatchersSugar.*
import org.mockito.IdiomaticMockito.StubbingOps
import org.mockito.MockitoSugar.mock
import org.scalatheagorist.freeflowfeedszio.AppConfig
import org.scalatheagorist.freeflowfeedszio.core.fs.FileStoreClient
import org.scalatheagorist.freeflowfeedszio.core.http.HttpClient
import org.scalatheagorist.freeflowfeedszio.models.Article
import org.scalatheagorist.freeflowfeedszio.models.RSSFeed
import org.scalatheagorist.freeflowfeedszio.publisher.Lang
import org.scalatheagorist.freeflowfeedszio.publisher.Publisher
import org.scalatheagorist.freeflowfeedszio.publisher.PublisherHost
import zio.ZIO
import zio.ZLayer
import zio.http.Body
import zio.http.Client
import zio.http.Header
import zio.http.Headers
import zio.http.Response
import zio.stream.ZStream
import zio.test.ZIOSpecDefault
import zio.test._

object HtmlScrapeServiceSpec extends ZIOSpecDefault {
  private val appConfig = mock[AppConfig]
  private val fileStoreClient = mock[FileStoreClient]
  private val httpClient = mock[HttpClient]
  private val client = mock[Client]

  private val appConfigLayer =
    ZLayer(ZIO.succeed(appConfig))

  private val clientLayer =
    ZLayer.succeed(client)

  private val httpClientLayer =
    clientLayer >>> ZLayer.succeed(httpClient)

  private val fileStoreClientLayer =
    ZLayer.succeed(fileStoreClient)

  override def spec = suite("HtmlScrapeService")(
    suite("stream") {
      test("must work") {
        val rssFeed = RSSFeed(
          "Schweizer Monat",
          Article(
            "\u00abMan findet die Macht  nie dort, wo man sie sucht\u00bb",
            "https://schweizermonat.ch/man-findet-die-macht-nie-dort-wo-man-sie-sucht/"
          ),
          Publisher.SCHWEIZERMONAT,
          Lang.DE
        )
        val headers: Headers =
          Headers(Header.Accept.parse("text/html; charset=utf-8").toOption.toList ::: Header.ContentType.parse("text/html; charset=utf-8").toOption.toList)
        val rssFeedJson =
          """{"author": "Schweizer Monat", "article": {"title": "\u00abMan findet die Macht  nie dort, wo man sie sucht\u00bb", "link": "https://schweizermonat.ch/man-findet-die-macht-nie-dort-wo-man-sie-sucht/"}, "publisher": "SCHWEIZER_MONAT", "lang": "DE"}""".stripMargin

        // mock
        appConfig.hosts returns List(PublisherHost("https://schweizermonat.ch/", "archivperlen/", 0, Publisher.SCHWEIZERMONAT))
        appConfig.initialReverse returns false
        appConfig.scrapeConcurrency returns 1
        httpClient.get(*, headers) returns ZIO.succeed(Response(body = Body.fromString(rssFeedJson))).provideLayer(*)
        fileStoreClient.saveIntoDir(ZStream(rssFeed)) returns ZStream.unit.provideLayer(*)

        // act & assert
        ZIO
          .serviceWithZIO[HtmlScrapeService](_.stream.runLast.provideLayer(clientLayer))
          .map {
            case Some(_) => assertTrue(true)
            case None => assertTrue(false)
          }
          .provideLayer((
              httpClientLayer ++
              appConfigLayer ++
              fileStoreClientLayer
            ) >>> HtmlScrapeService.layer)
      }
    })
}
