package org.scalatheagorist.freeflowfeedszio.publisher.models

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import org.scalatheagorist.freeflowfeedszio.models.HtmlResponse
import org.scalatheagorist.freeflowfeedszio.models.Feed
import zio.stream.ZStream

trait PublisherModel extends JsoupBrowser:
    def toFeedStream(htmlResponse: HtmlResponse): ZStream[Any, Throwable, Feed]
