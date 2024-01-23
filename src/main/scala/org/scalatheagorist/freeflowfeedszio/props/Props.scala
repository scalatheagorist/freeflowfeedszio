package org.scalatheagorist.freeflowfeedszio.props

import caliban.schema.Schema
import caliban.schema.Schema.auto.*
import caliban.schema.Types
import cats.Show
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import org.scalatheagorist.freeflowfeedszio.models.Feed
import org.scalatheagorist.freeflowfeedszio.models.HtmlResponse
import org.scalatheagorist.freeflowfeedszio.props.Props.*
import org.scalatheagorist.freeflowfeedszio.props.Props.Publisher.*
import org.scalatheagorist.freeflowfeedszio.props.models.*
import org.scalatheagorist.freeflowfeedszio.util.RichURL.*
import zio.stream.ZStream

sealed trait Props

object Props:
  sealed trait Publisher      extends Props
  case object EFMAGAZIN       extends Publisher
  case object FREIHEITSFUNKEN extends Publisher
  case object MISESDE         extends Publisher
  case object SCHWEIZER_MONAT extends Publisher

  object Publisher:
    given show: Show[Publisher] with
      def show(publisher: Publisher): String = publisher match
        case EFMAGAZIN       => "EFMAGAZIN"
        case FREIHEITSFUNKEN => "FREIHEITSFUNKEN"
        case MISESDE         => "MISESDE"
        case SCHWEIZER_MONAT => "SCHWEIZER_MONAT"

    inline def from(publisher: String): Publisher = publisher match
      case "EFMAGAZIN"       => EFMAGAZIN
      case "FREIHEITSFUNKEN" => FREIHEITSFUNKEN
      case "MISESDE"         => MISESDE
      case "SCHWEIZER_MONAT" => SCHWEIZER_MONAT

  sealed trait Lang extends Props
  case object EN    extends Lang
  case object DE    extends Lang

  object Lang:
    given show: Show[Lang] with
      def show(lang: Lang): String = lang match
        case EN => "EN"
        case DE => "DE"

    def from(lang: String): Lang = lang match
      case "EN" => EN
      case "DE" => DE

  given Schema[Any, Publisher] = Schema.gen
  given Schema[Any, Lang]      = Schema.gen
  given Schema[Any, Props]     = Schema.gen
