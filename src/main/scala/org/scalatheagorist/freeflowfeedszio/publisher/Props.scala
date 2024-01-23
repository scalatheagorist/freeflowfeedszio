package org.scalatheagorist.freeflowfeedszio.publisher

import caliban.schema.Schema
import caliban.schema.Types
import cats.Show
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import org.scalatheagorist.freeflowfeedszio.models.HtmlResponse
import org.scalatheagorist.freeflowfeedszio.models.Feed
import org.scalatheagorist.freeflowfeedszio.publisher.Props.*
import org.scalatheagorist.freeflowfeedszio.publisher.Props.Publisher.*
import org.scalatheagorist.freeflowfeedszio.publisher.models.*
import org.scalatheagorist.freeflowfeedszio.util.RichURL.*
import zio.stream.ZStream

sealed trait Props derives Schema.Auto

object Props:
  enum Publisher extends Props:
    case EFMAGAZIN, FREIHEITSFUNKEN, MISESDE, SCHWEIZER_MONAT

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

  enum Lang extends Props:
    case EN, DE

  object Lang:
    given show: Show[Lang] with
      def show(lang: Lang): String = lang match
        case EN => "EN"
        case DE => "DE"

    def from(lang: String): Lang = lang match
      case "EN" => EN
      case "DE" => DE

  given Schema[Any, Props] = Schema.Auto.derived
