package org.scalatheagorist.freeflowfeedszio.models

import org.scalatheagorist.freeflowfeedszio.publisher.Category.Publisher

final case class HtmlResponse(publisher: Publisher, response: String)
