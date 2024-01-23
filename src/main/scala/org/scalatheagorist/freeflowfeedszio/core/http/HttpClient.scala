package org.scalatheagorist.freeflowfeedszio.core.http

import zio.*
import zio.http.*

trait HttpClient:
  def get(url: URL): ZIO[Client & Scope, Throwable, Response]

object HttpClient:
  val live: ULayer[HttpClient] =
    ZLayer.succeed(
      new HttpClient {
        override def get(url: URL): ZIO[Client & Scope, Throwable, Response] =
          ZClient.request(Request.get(url))
      }
    )
