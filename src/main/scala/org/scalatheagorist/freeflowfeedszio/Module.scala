package org.scalatheagorist.freeflowfeedszio

import cats.implicits.toShow
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import org.scalatheagorist.freeflowfeedszio.core.fs.FileStoreClient
import org.scalatheagorist.freeflowfeedszio.core.fs.models.FileStoreConfig
import org.scalatheagorist.freeflowfeedszio.core.http.HttpClient
import org.scalatheagorist.freeflowfeedszio.services.HtmlScrapeService
import org.scalatheagorist.freeflowfeedszio.services.RSSService
import org.slf4j.LoggerFactory
import zio.Exit
import zio.Trace
import zio.Unsafe

import java.io.File
import java.time.Clock

final class Module[+R](
    configFile: File)(
    implicit val runtime: zio.Runtime[R],
    trace: Trace
) extends AbstractModule with ScalaModule {
  private val logger = LoggerFactory.getLogger(classOf[Module[_]])
  private val clock = Clock.systemUTC()

  override def configure(): Unit = {
    Unsafe.unsafe { implicit unsafe =>
      runtime.unsafe.run(AppConfig.from(configFile)).map { appConfig =>
        logger.info(appConfig.show)
        appConfig
      }
    } match {
      case Exit.Success(appConfig) =>
        bind[Clock].toInstance(clock)

        bind[AppConfig].toInstance(appConfig)
        bind[HttpClient].asEagerSingleton()
        bind[FileStoreConfig].toInstance(appConfig.fileStoreConfig)
        bind[FileStoreClient].asEagerSingleton()
        bind[HtmlScrapeService].asEagerSingleton()
        bind[RSSService].asEagerSingleton()

        logger.debug("bound all modules")
      case Exit.Failure(err) =>
        err.squash.printStackTrace()
        System.exit(1)
    }
  }
}
