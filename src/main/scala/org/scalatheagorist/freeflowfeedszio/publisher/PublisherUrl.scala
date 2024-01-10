package org.scalatheagorist.freeflowfeedszio.publisher

import cats.Show
import cats.implicits.showInterpolator
import org.scalatheagorist.freeflowfeedszio.publisher.Props.*
import zio.http.URL
import zio.stream.ZStream

import java.net.URI
import scala.util.Try

final case class PublisherUrl(publisher: Publisher, url: URL)

extension (hosts: List[PublisherHost])
    def toPublisherUrl[R, E](initialReverse: Boolean): ZStream[R, E, PublisherUrl] =
        ZStream
            .fromIterable {
                lazy val publisherUrls = hosts.flatMap(_.toPublisherUrls).sortBy { case (_, url) =>
                    url
                        .split("page=")
                        .lastOption
                        .flatMap(p => Try(p.toInt).toOption)
                        .getOrElse(Int.MinValue)
                }

                if initialReverse then publisherUrls.reverse else publisherUrls
            }
            .flatMap { case (publisher, url) =>
                URL.fromURI(URI.create(url)) match
                    case Some(url) => ZStream.succeed(PublisherUrl(publisher, url))
                    case None      => ZStream.empty
            }
