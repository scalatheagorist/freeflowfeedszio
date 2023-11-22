package org.scalatheagorist.freeflowfeedszio.core.http

import zio.ZIO
import zio.http
import zio.http.Headers
import zio.http.Request
import zio.http.Response
import zio.http.URL
import zio.http.ZClient

final class HttpClient {
  def get(url: URL, headers: Headers): ZIO[http.Client, Throwable, Response] =
    ZClient
      .request(Request.get(url).addHeaders(headers))
      .tap(resp => ZIO.logInfo(s"STATUS ${resp.status.code} from ${url.encode}"))
}