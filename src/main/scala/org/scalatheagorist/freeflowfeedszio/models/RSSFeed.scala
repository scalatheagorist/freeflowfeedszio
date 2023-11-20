package org.scalatheagorist.freeflowfeedszio.models

import org.scalatheagorist.freeflowfeedszio.publisher.Lang
import org.scalatheagorist.freeflowfeedszio.publisher.Publisher
import zio.json._

final case class RSSFeed(author: String, article: Article, publisher: Publisher, lang: Lang)

object RSSFeed {
  implicit val encoder: JsonEncoder[RSSFeed] = DeriveJsonEncoder.gen[RSSFeed]
  implicit val decoder: JsonDecoder[RSSFeed] = DeriveJsonDecoder.gen[RSSFeed]
}
