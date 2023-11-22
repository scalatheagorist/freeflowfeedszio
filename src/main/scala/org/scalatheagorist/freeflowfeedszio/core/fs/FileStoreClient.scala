package org.scalatheagorist.freeflowfeedszio.core.fs

import org.scalatheagorist.freeflowfeedszio.core.fs.models.FileStoreConfig
import org.scalatheagorist.freeflowfeedszio.util.HashSupport
import zio.Chunk
import zio.ZIO
import zio.ZLayer
import zio.json._
import zio.nio.charset.Charset
import zio.nio.file.Files
import zio.nio.file.Path
import zio.stream.ZStream

import java.io.IOException

trait FileStoreClient {
  def saveIntoDir[R, A](
    stream: ZStream[R, Throwable, A])(
    implicit encoder: JsonEncoder[A]
  ): ZStream[R, Throwable, Unit]

  def loadFromDir: ZStream[Any, IOException, Chunk[Byte]]
}

object FileStoreClient {
  val layer: ZLayer[FileStoreConfig, Nothing, FileStoreClient] =
    ZLayer {
      ZIO.service[FileStoreConfig].map { config =>
        new FileStoreClient {
          val dir: Path = Path(config.path)

          override def saveIntoDir[R, A](
            stream: ZStream[R, Throwable, A])(
            implicit encoder: JsonEncoder[A]
          ): ZStream[R, Throwable, Unit] =
            stream.flatMapPar(config.concurrency) { value: A =>
              for {
                jsonContent <- ZStream(value.toJson)
                fileName    <- ZStream.fromZIO(HashSupport.md5(jsonContent)).map(fileName => s"data_$fileName.json")
                filePath    <- ZStream.succeed(dir / Path(fileName))
                _           <- ZStream.whenCaseZIO(Files.exists(filePath)) {
                                 case true => ZStream.logDebug(s"file $fileName already exists")
                                 case false =>
                                   for {
                                     chunk <- ZStream.fromZIO(Charset.Standard.utf8.encodeString(jsonContent))
                                     _     <- ZStream.fromZIO(Files.writeBytes(filePath, chunk))
                                   } yield ()
                               }
              } yield ()
            }

          override def loadFromDir: ZStream[Any, IOException, Chunk[Byte]] =
            Files.list(dir).map(_.toAbsolutePath).flatMapPar(config.concurrency) { path =>
              (for {
                pathStream <- ZStream.fromZIO(path)
                chunk      <- ZStream.fromZIO(Files.readAllBytes(pathStream))
              } yield chunk)
                .tapError(ex => ZIO.logError(ex.getMessage))
                .mapError(ex => new IOException(ex))
            }
        }
      }
    }
}
