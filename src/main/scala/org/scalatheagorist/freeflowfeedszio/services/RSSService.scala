package org.scalatheagorist.freeflowfeedszio.services

import org.scalatheagorist.freeflowfeedszio.AppConfig
import org.scalatheagorist.freeflowfeedszio.core.fs.FileStoreClient
import org.scalatheagorist.freeflowfeedszio.models.RSSFeed
import org.scalatheagorist.freeflowfeedszio.publisher.Lang
import org.scalatheagorist.freeflowfeedszio.publisher.Publisher
import org.scalatheagorist.freeflowfeedszio.view.RSSBuilder
import zio.&
import zio.Clock
import zio.Config.LocalTime
import zio.ZIO
import zio.ZLayer
import zio.durationLong
import zio.http.Client
import zio.json._
import zio.stream.ZSink
import zio.stream.ZStream

import java.time.Duration
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.{Clock => JavaClock}

trait RSSService {
  def generateFeeds(publisher: Option[Publisher], lang: Option[Lang]): ZStream[Any, Throwable, CharSequence]
  def runScraper: ZIO[Client, Throwable, Unit]
}

object RSSService {
  val layer: ZLayer[JavaClock & RSSBuilder & FileStoreClient & HtmlScrapeService & AppConfig, Nothing, RSSService] =
    ZLayer {
      for {
        appConfig         <- ZIO.service[AppConfig]
        htmlScrapeService <- ZIO.service[HtmlScrapeService]
        fileStoreClient   <- ZIO.service[FileStoreClient]
        rssBuilder        <- ZIO.service[RSSBuilder]
        clock             <- ZIO.service[JavaClock]
      } yield new RSSService {
        private val zioClock: Clock.ClockJava = Clock.ClockJava(clock)

        def generateFeeds(publisher: Option[Publisher], lang: Option[Lang]): ZStream[Any, Throwable, CharSequence] =
          rssBuilder.build(publisher, lang) {
            fileStoreClient.loadFromDir.flatMap { chunk =>
              ZStream(new String(chunk.toArray, "UTF-8").fromJson[RSSFeed]).flatMap {
                case Right(rssFeed) =>
                  ZStream.succeed(rssFeed)
                case Left(errorMsg) =>
                  ZStream.empty <* ZStream.logWarning(s"could not parse json with message: $errorMsg")
              }
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
