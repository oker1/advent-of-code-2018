package aoc

import java.nio.file.Paths
import java.util.concurrent.Executors

import cats.effect.{ContextShift, IO, Resource, Sync}
import fs2.{io, text}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

object util {
  val blockingExecutionContext: Resource[IO, ExecutionContextExecutorService] =
    Resource.make(
      IO(ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2)))
    )(ec => IO(ec.shutdown()))

  def readInput[F[_]: Sync: ContextShift](
    file: String,
    blockingEC: ExecutionContext
  ): fs2.Stream[F, String] =
    io.file
      .readAll[F](Paths.get(file), blockingEC, 4096)
      .through(text.utf8Decode)
      .through(text.lines)
      .filter(s => !s.trim.isEmpty)
}
