package org.scalatheagorist.freeflowfeedszio.services

import org.scalatheagorist.freeflowfeedszio.Configuration
import org.scalatheagorist.freeflowfeedszio.core.http.HttpClient
import org.scalatheagorist.freeflowfeedszio.core.jdbc.DatabaseClient
import org.scalatheagorist.freeflowfeedszio.models.HtmlResponse
import org.scalatheagorist.freeflowfeedszio.publisher.Hosts.RichHosts
import org.scalatheagorist.freeflowfeedszio.publisher.Publisher
import zio._
import zio.http.Client
import zio.prelude.AssociativeBothTuple4Ops
import zio.stream.ZPipeline.utf8Decode
import zio.stream.ZStream

import java.time.{Clock => JavaClock}

trait HtmlScrapeService {
  def stream: ZStream[Client, Throwable, Unit]
}

object HtmlScrapeService {
  private val scrapeInfo =
    """
      |
      | start scraping
      |
      |""".stripMargin

  val layer: ZLayer[JavaClock & Configuration & HttpClient & DatabaseClient, Nothing, HtmlScrapeService] =
    ZLayer {
      (
        ZIO.service[Configuration],
        ZIO.service[HttpClient],
        ZIO.service[DatabaseClient],
        ZIO.service[JavaClock]
      ).mapN(
        (configuration, httpClient, databaseClient, clock) =>
          new HtmlScrapeService {
            override def stream: ZStream[Client, Throwable, Unit] =
              (for {
                _             <- ZStream.logInfo(scrapeInfo)

                publisherUrls  = configuration.hosts.toPublisherUrl(configuration.initialReverse)

                inserted       = databaseClient.insert(stream =
                                   publisherUrls.flatMapPar(configuration.scrapeConcurrency) { url =>
                                     ZStream.blocking {
                                       (for {
                                         response <- ZStream.fromZIO(httpClient.get(url.url)).tapError(ex => ZIO.logError(ex.getMessage))

                                         _        <- ZStream.logInfo(s"STATUS ${response.status.code} from ${url.url.encode}")

                                         decoded  <- (response.body.asStream.tapError(ex => ZIO.logError(ex.getMessage)) >>> utf8Decode).orElse(ZStream.empty)
                                         htmlResp <- ZStream.succeed(HtmlResponse(url.publisher, decoded))
                                         rssFeed  <- Publisher.toRSSFeedStream(htmlResp, url).map(_.toDbRssFeeds(clock))
                                       } yield rssFeed).tapError(ex => ZIO.logError(ex.getMessage))
                                     }
                                   }.runCollect
                                 )
                _              <- ZStream.fromZIO(inserted) // exec
              } yield ()).tapError(ex => ZIO.logError(ex.getMessage))
          }
      )
    }
}
