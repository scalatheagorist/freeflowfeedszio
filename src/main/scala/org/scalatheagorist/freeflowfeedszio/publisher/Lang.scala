package org.scalatheagorist.freeflowfeedszio.publisher

import cats.Show
import zio.json.JsonDecoder
import zio.json.JsonEncoder

sealed trait Lang extends Product with Serializable

object Lang {
  case object EN extends Lang
  case object DE extends Lang

  implicit val show: Show[Lang] = Show {
    case EN => "EN"
    case DE => "DE"
  }

  implicit val encoder: JsonEncoder[Lang] = JsonEncoder.string.contramap {
    case EN => "EN"
    case DE => "DE"
  }

  implicit val decoder: JsonDecoder[Lang] = JsonDecoder.string.map {
    case "EN" => EN
    case "DE" => DE
  }

  def from(lang: String): Lang = lang match {
    case "EN" => EN
    case "DE" => DE
  }
}
