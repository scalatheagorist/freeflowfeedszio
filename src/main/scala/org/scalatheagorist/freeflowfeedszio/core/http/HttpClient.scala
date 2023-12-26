package org.scalatheagorist.freeflowfeedszio.core.http

import zio.ZIO
import zio.ZLayer
import zio.http.Client
import zio.http.Request
import zio.http.Response
import zio.http.URL
import zio.http.ZClient

trait HttpClient {
  def get(url: URL): ZIO[Client, Throwable, Response]
}

object HttpClient {
  val live: ZLayer[Any, Nothing, HttpClient] =
    ZLayer.succeed(
      new HttpClient {
        override def get(url: URL): ZIO[Client, Throwable, Response] =
          ZClient.request(Request.get(url))
      }
    )
}
