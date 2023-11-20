package org.scalatheagorist.freeflowfeedszio.services

import org.scalatheagorist.freeflowfeedszio.AppConfig
import org.scalatheagorist.freeflowfeedszio.core.fs.FileStoreClient
import org.scalatheagorist.freeflowfeedszio.models.RSSFeed
import org.scalatheagorist.freeflowfeedszio.publisher.Lang
import org.scalatheagorist.freeflowfeedszio.publisher.Publisher
import org.scalatheagorist.freeflowfeedszio.view.RSSBuilder
import zio.Clock
import zio.Config.LocalTime
import zio.ZIO
import zio.durationLong
import zio.http.Client
import zio.json._
import zio.stream.ZSink
import zio.stream.ZStream

import java.time.Duration
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import javax.inject.Inject

final class RSSService @Inject()(
    appConfig: AppConfig,
    htmlScrapeService: HtmlScrapeService,
    fileStoreClient: FileStoreClient,
    rssBuilder: RSSBuilder
) {
  def generateFeeds(publisher: Option[Publisher], lang: Option[Lang]): ZStream[Any, Throwable, CharSequence] =
    rssBuilder.build(publisher, lang) {
      fileStoreClient.loadFromDir.flatMap { chunk =>
        ZStream(new String(chunk.toArray, "UTF-8").fromJson[RSSFeed]).flatMap {
          case Right(rssFeed) =>
            ZStream.succeed(rssFeed)
          case Left(ex) =>
            ZStream.empty <* ZStream.logWarning(s"could not parse json with message: $ex")
        }
      }
    }.tapError(ex => ZIO.logError(ex.getMessage))

  def scrapeWithInterval: ZIO[Client, Throwable, Unit] = {
    val targetTime = for {
      time           <- ZIO.fromEither(LocalTime.parse(appConfig.update))
      date           <- ZIO.attemptBlocking(LocalDate.now())
      offsetDateTime  = OffsetDateTime.of(date, time, ZoneOffset.UTC)
    } yield offsetDateTime

    targetTime.flatMap { targetTime =>
      def pushLoop: ZIO[Client, Throwable, Unit] =
        for {
          currentTime   <- Clock.currentDateTime
          delay         <- ZIO.attempt(Duration.between(currentTime, targetTime))
          adjustedDelay  = if (delay.isNegative) delay.plusHours(appConfig.updateInterval) else delay

          _             <- ZIO.sleep(adjustedDelay.toMillis.millis)
          _             <- htmlScrapeService.stream.run(ZSink.drain).forkDaemon
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
