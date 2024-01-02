package org.scalatheagorist.freeflowfeedszio.services

import org.scalatheagorist.freeflowfeedszio.Configuration
import org.scalatheagorist.freeflowfeedszio.core.jdbc.RssFeedsDatabaseService
import org.scalatheagorist.freeflowfeedszio.models.RSSFeed
import org.scalatheagorist.freeflowfeedszio.publisher.Props
import org.scalatheagorist.freeflowfeedszio.view.RSSBuilder
import zio.*
import zio.Config.LocalTime
import zio.http.Client
import zio.prelude.AssociativeBothTuple5Ops
import zio.stream.ZSink
import zio.stream.ZStream

import java.time.Clock as JavaClock
import java.time.Duration
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset

trait RSSService:
  def getFeeds(
    page: Int,
    pageSize: Int,
    props: Option[Props],
    term: Option[String]
  ): ZStream[Any, Throwable, String]

  def runScraper: ZIO[Client, Throwable, Unit]

object RSSService:
  val layer: ZLayer[JavaClock & Configuration & RSSBuilder & RssFeedsDatabaseService & HtmlScrapeService, Nothing, RSSService] =
    ZLayer {
      (
        ZIO.service[Configuration],
        ZIO.service[HtmlScrapeService],
        ZIO.service[RssFeedsDatabaseService],
        ZIO.service[RSSBuilder],
        ZIO.serviceWith[JavaClock](Clock.ClockJava)
      ).mapN { (configuration, htmlScrapeService, databaseClient, rssBuilder, zioClock) =>
        new RSSService {
          def getFeeds(
            page: Int,
            pageSize: Int,
            props: Option[Props],
            term: Option[String]
          ): ZStream[Any, Throwable, String] =
            rssBuilder
              .build(props) {
                databaseClient.select(page, pageSize, props, term).flatMap { rssFeed =>
                  ZStream.fromZIO(ZIO.attempt(RSSFeed.from(rssFeed)))
                }
              }
              .tapError(ex => ZIO.logError(ex.getMessage))

          def runScraper: ZIO[Client, Throwable, Unit] =
            val targetTime =
              for
                time           <- ZIO.fromEither(LocalTime.parse(configuration.update))
                date           <- ZIO.attempt(LocalDate.now(zioClock.clock))
                offsetDateTime <- ZIO.attempt(OffsetDateTime.of(date, time, ZoneOffset.UTC))
              yield offsetDateTime

            targetTime.flatMap { targetTime =>
              /**
               * sleep 1 second to begin a new loop session without multiple runs at the moment
               */
              def pushLoop: ZIO[Client, Throwable, Unit] =
                for
                  currentTime  <- zioClock.currentDateTime
                  delay        <- ZIO.attempt(Duration.between(currentTime, targetTime))
                  adjustedDelay = if delay.isNegative then delay.plusHours(configuration.updateInterval) else delay
                  _            <- ZIO.sleep(adjustedDelay.toMillis.millis)
                  _            <- htmlScrapeService.stream.run(ZSink.drain).forkDaemon
                  _            <- ZIO.sleep(1.second)
                  _            <- pushLoop
                yield ()

              if targetTime.isAfter(OffsetDateTime.MIN) && targetTime.isBefore(OffsetDateTime.MAX) then pushLoop
              else ZIO.logError(s"invalid time: ${configuration.update}")
            }
        }
      }
    }
