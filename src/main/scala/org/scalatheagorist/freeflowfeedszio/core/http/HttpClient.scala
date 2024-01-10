package org.scalatheagorist.freeflowfeedszio.core.http

import zio.*
import zio.http.*

trait HttpClient:
    def get(url: URL): ZIO[Client, Throwable, Response]

object HttpClient:
    val live: ZLayer[Any, Nothing, HttpClient] =
        ZLayer.succeed(
          new HttpClient {
              override def get(url: URL): ZIO[Client, Throwable, Response] =
                  ZClient.request(Request.get(url))
          }
        )
