package org.scalatheagorist.freeflowfeedszio.services

import org.scalatheagorist.freeflowfeedszio.Configuration
import org.scalatheagorist.freeflowfeedszio.core.http.HttpClient
import org.scalatheagorist.freeflowfeedszio.core.jdbc.FeedsDatabaseService
import org.scalatheagorist.freeflowfeedszio.models.HtmlResponse
import org.scalatheagorist.freeflowfeedszio.models.Feed
import org.scalatheagorist.freeflowfeedszio.props.*
import org.scalatheagorist.freeflowfeedszio.props.Props.Publisher
import zio.*
import zio.http.Client
import zio.prelude.AssociativeBothTuple4Ops
import zio.stream.ZPipeline.utf8Decode
import zio.stream.ZStream

import java.time.Clock as JavaClock

trait HtmlScrapeService:
  def stream: ZStream[Client & Scope, Throwable, Unit]

object HtmlScrapeService:
  private val scrapeInfo = "\nstart scraping\n"

  val layer: ZLayer[JavaClock & Configuration & HttpClient & FeedsDatabaseService, Nothing, HtmlScrapeService] =
    ZLayer {
      (
        ZIO.service[Configuration],
        ZIO.service[HttpClient],
        ZIO.service[FeedsDatabaseService],
        ZIO.service[JavaClock]
      ).mapN((configuration, httpClient, databaseClient, clock) =>
        new HtmlScrapeService {
          override def stream: ZStream[Client & Scope, Throwable, Unit] =
            (for
              _            <- ZStream.logInfo(scrapeInfo)
              publisherUrls = configuration.hosts.toPublisherUrl(configuration.initialReverse)
              _            <-
                databaseClient.insert(stream = publisherUrls.flatMapPar(configuration.scrapeConcurrency) { url =>
                  ZStream.blocking {
                    (for
                      response <- ZStream
                                    .fromZIO(httpClient.get(url.url))
                                    .tapError(ex => ZIO.logError(ex.getMessage))
                      _        <- ZStream.logInfo(
                                    s"STATUS ${response.status.code} from ${url.url.encode}"
                                  )
                      decoded  <- (response.body.asStream.tapError(ex => ZIO.logError(ex.getMessage)) >>> utf8Decode)
                                    .orElse(ZStream.empty)
                      htmlResp <- ZStream.succeed(HtmlResponse(url.publisher, decoded))
                      feed     <- Feed.from(htmlResp, url).map(_.toFeedRow(clock))
                    yield feed).tapError(ex => ZIO.logError(ex.getMessage))
                  }
                })
            yield ()).tapError(ex => ZIO.logError(ex.getMessage))
        }
      )
    }
