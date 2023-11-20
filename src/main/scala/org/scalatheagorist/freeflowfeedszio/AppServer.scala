package org.scalatheagorist.freeflowfeedszio

import com.google.inject.Guice
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import org.scalatheagorist.freeflowfeedszio.services.RSSService
import zio.Duration
import zio.ZIOAppDefault
import zio._
import zio.http._
import zio.interop.catz.implicits.rts

import java.io.File
import java.util.concurrent.TimeUnit

object AppServer extends ZIOAppDefault { self =>
  private val zClientLayer: ZLayer[Any, Throwable, Client] = ZLayer.suspend(Client.default)

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    (for {
      baseDir    <- ZIO.attempt(new File(java.lang.System.getProperty("user.dir")))
      _          <- ZIO.logInfo(s"baseDir: ${baseDir.getAbsolutePath}")

      confDir    <- ZIO.attempt(new File(baseDir, "src/main/resources/application.conf"))
      _          <- ZIO.logInfo(s"conf dir: ${confDir.getAbsolutePath}")
      injector   <- ZIO.attempt(Guice.createInjector(new Module(confDir)))

      port       <- ZIO.attempt(injector.instance[AppConfig].httpServerPort)
      rssService <- ZIO.attempt(injector.instance[RSSService])

      _          <- rssService.scrapeWithInterval.forkDaemon

      _          <- ZIO.logInfo(s"Server started @ http://0.0.0.0:$port")
      _          <- Server
                      .serve(Routes(rssService).withDefaultErrorResponse)
                      .timeout(Duration(600L, TimeUnit.SECONDS))
                      .provide(Server.defaultWithPort(port))
    } yield ()).provideLayer(zClientLayer).exitCode
}
