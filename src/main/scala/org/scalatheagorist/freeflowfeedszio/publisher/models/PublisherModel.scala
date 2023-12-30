package org.scalatheagorist.freeflowfeedszio.publisher.models

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import org.scalatheagorist.freeflowfeedszio.models.HtmlResponse
import org.scalatheagorist.freeflowfeedszio.models.RSSFeed
import zio.stream.ZStream

trait PublisherModel extends JsoupBrowser:
  def toRSSFeedStream(htmlResponse: HtmlResponse): ZStream[Any, Throwable, RSSFeed]
