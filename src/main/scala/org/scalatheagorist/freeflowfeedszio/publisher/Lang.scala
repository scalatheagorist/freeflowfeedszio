package org.scalatheagorist.freeflowfeedszio.publisher

import zio.json.JsonDecoder
import zio.json.JsonEncoder

sealed trait Lang extends Product with Serializable

object Lang {
  case object EN extends Lang
  case object DE extends Lang

  implicit val encoder: JsonEncoder[Lang] = JsonEncoder.string.contramap {
    case EN => "EN"
    case DE => "DE"
  }

  implicit val decoder: JsonDecoder[Lang] = JsonDecoder.string.map {
    case "EN" => EN
    case "DE" => DE
  }
}
