package org.scalatheagorist.freeflowfeedszio.util

import zio.http.URL

import java.net

object RichURL {
  implicit class RichUrl(url: => URL) {
    /**
     * https://www.xyz.com
     */
    def toProtocolWithHost: String = {
      val javaUrl: net.URL = url.toJavaURL.get
      javaUrl.getProtocol + "://www." + javaUrl.getHost
    }
  }
}
