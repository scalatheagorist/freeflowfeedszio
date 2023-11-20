package org.scalatheagorist.freeflowfeedszio.models

import zio.json._

final case class Article(title: String, link: String)

object Article {
  implicit val encoder: JsonEncoder[Article] = DeriveJsonEncoder.gen[Article]
  implicit val decoder: JsonDecoder[Article] = DeriveJsonDecoder.gen[Article]
}
