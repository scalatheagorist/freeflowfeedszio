package org.scalatheagorist.freeflowfeedszio.models

import org.scalatheagorist.freeflowfeedszio.publisher.Publisher

final case class HtmlResponse(publisher: Publisher, response: String)
