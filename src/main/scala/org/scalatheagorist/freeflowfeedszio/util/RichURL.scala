package org.scalatheagorist.freeflowfeedszio.util

import zio.http.URL

import java.net

object RichURL:
    extension (url: URL)
        def toProtocolWithHost: String =
            val javaUrl: net.URL = url.toJavaURL.get
            // example: https://www.xyz.com
            javaUrl.getProtocol + "://www." + javaUrl.getHost
