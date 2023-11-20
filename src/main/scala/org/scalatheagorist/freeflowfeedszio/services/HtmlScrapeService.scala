package org.scalatheagorist.freeflowfeedszio.services

import org.scalatheagorist.freeflowfeedszio.AppConfig
import org.scalatheagorist.freeflowfeedszio.core.fs.FileStoreClient
import org.scalatheagorist.freeflowfeedszio.core.http.HttpClient
import org.scalatheagorist.freeflowfeedszio.models.HtmlResponse
import org.scalatheagorist.freeflowfeedszio.publisher.Hosts.RichHosts
import org.scalatheagorist.freeflowfeedszio.publisher.Publisher
import org.scalatheagorist.freeflowfeedszio.services.HtmlScrapeService.HeaderError
import zio._
import zio.http.Client
import zio.http.Header
import zio.http.Headers
import zio.stream.ZPipeline.utf8Decode
import zio.stream.ZStream

import javax.inject.Inject

final class HtmlScrapeService @Inject()(httpClient: HttpClient, appConfig: AppConfig, fileStoreClient: FileStoreClient) {
  def stream: ZStream[Client, Throwable, Unit] =
    (for {
      accept        <- ZStream.fromZIO(ZIO.fromEither[String, Header.Accept](Header.Accept.parse("text/html; charset=utf-8"))).mapError(HeaderError)
      contentType   <- ZStream.fromZIO(ZIO.fromEither[String, Header.ContentType](Header.ContentType.parse("text/html; charset=utf-8"))).mapError(HeaderError)
      publisherUrls  = appConfig.hosts.toPublisherUrl[Client, Throwable](appConfig.initialReverse)

      _             <- ZStream.logInfo("start scraping")

      _             <- publisherUrls.flatMapPar(appConfig.scrapeConcurrency) { url =>
                         fileStoreClient.saveIntoDir(
                           (for {
                             _        <- ZStream.logInfo(s"scrape data from ${url.url.encode}")
                             response <- ZStream.fromZIO(httpClient.get(url.url, Headers(accept :: contentType :: Nil)))
                             htmlResp <- (response.body.asStream >>> utf8Decode).map(HtmlResponse(url.publisher, _))
                             rssFeed  <- Publisher.toRSSFeedStream(htmlResp, url)
                           } yield rssFeed).tapError(ex => ZIO.logWarning(ex.getMessage))
                         ).tapError(ex => ZIO.logWarning(ex.getMessage))
                       }
    } yield ()).tapError(ex => ZIO.logError(ex.getMessage))
}

object HtmlScrapeService {
  final case class HeaderError(header: String) extends Throwable(header)
}
