package org.scalatheagorist.freeflowfeedszio.services

import org.scalatheagorist.freeflowfeedszio.AppConfig
import org.scalatheagorist.freeflowfeedszio.core.jdbc.DatabaseClient
import org.scalatheagorist.freeflowfeedszio.models.RSSFeed
import org.scalatheagorist.freeflowfeedszio.publisher.Lang
import org.scalatheagorist.freeflowfeedszio.publisher.Publisher
import org.scalatheagorist.freeflowfeedszio.view.RSSBuilder
import zio.Config.LocalTime
import zio._
import zio.http.Client
import zio.prelude.AssociativeBothTuple5Ops
import zio.stream.ZSink
import zio.stream.ZStream

import java.time.Duration
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.{Clock => JavaClock}

trait RSSService {
  def getFeeds(
    page: Int,
    pageSize: Int,
    publisher: Option[Publisher],
    lang: Option[Lang],
    term: Option[String]
  ): ZStream[Any, Throwable, String]

  def runScraper: ZIO[Client, Throwable, Unit]
}

object RSSService {
  val layer: ZLayer[JavaClock & AppConfig & RSSBuilder & DatabaseClient & HtmlScrapeService, Nothing, RSSService] =
    ZLayer {
      (
        ZIO.service[AppConfig],
        ZIO.service[HtmlScrapeService],
        ZIO.service[DatabaseClient],
        ZIO.service[RSSBuilder],
        ZIO.serviceWith[JavaClock](Clock.ClockJava)
      ).mapN {
        (appConfig, htmlScrapeService, databaseClient, rssBuilder, zioClock) =>
          new RSSService {
            def getFeeds(
              page: Int,
              pageSize: Int,
              publisher: Option[Publisher],
              lang: Option[Lang],
              term: Option[String]
            ): ZStream[Any, Throwable, String] =
              rssBuilder.build(publisher, lang) {
                databaseClient.select(page, pageSize, publisher, lang, term).flatMap { rssFeed =>
                  ZStream.fromZIO(ZIO.attempt(RSSFeed.from(rssFeed)))
                }
              }.tapError(ex => ZIO.logError(ex.getMessage))

            def runScraper: ZIO[Client, Throwable, Unit] = {
              val targetTime =
                for {
                  time           <- ZIO.fromEither(LocalTime.parse(appConfig.update))
                  date           <- ZIO.attempt(LocalDate.now(zioClock.clock))
                  offsetDateTime <- ZIO.attempt(OffsetDateTime.of(date, time, ZoneOffset.UTC))
                } yield offsetDateTime


              targetTime.flatMap { targetTime =>
                def pushLoop: ZIO[Client, Throwable, Unit] =
                  for {
                    currentTime   <- zioClock.currentDateTime
                    delay         <- ZIO.attempt(Duration.between(currentTime, targetTime))
                    adjustedDelay  = if (delay.isNegative) delay.plusHours(appConfig.updateInterval) else delay

                    _             <- ZIO.sleep(adjustedDelay.toMillis.millis)
                    _             <- htmlScrapeService.stream.run(ZSink.drain).forkDaemon
                                     // sleep 1 second to begin a new loop session without multiple runs at the moment
                    _             <- ZIO.sleep(1.second)

                    _             <- pushLoop
                  } yield ()

                if (targetTime.isAfter(OffsetDateTime.MIN) && targetTime.isBefore(OffsetDateTime.MAX))
                  pushLoop
                else
                  ZIO.logError(s"invalid time: ${appConfig.update}")
              }
            }
          }
      }
    }
}
