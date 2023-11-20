package org.scalatheagorist.freeflowfeedszio.util

import zio.Task
import zio.ZIO

object HashSupport {
  def md5(s: String): Task[String] = ZIO.attempt {
    val m = java.security.MessageDigest.getInstance("MD5")
    val b = s.getBytes("UTF-8")
    m.update(b, 0, b.length)
    new java.math.BigInteger(1, m.digest()).toString(16).reverse.padTo(32, "0").reverse.mkString
  }
}
