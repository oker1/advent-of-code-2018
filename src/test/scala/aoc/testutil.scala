package aoc
import cats.effect.{ContextShift, IO}
import aoc.util._
import fs2._

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

object testutil {
  implicit val contextShift: ContextShift[IO] =
    IO.contextShift(ExecutionContext.global)

  def runStream[A](
    stream: ExecutionContextExecutorService => Stream[IO, A]
  ): List[A] = {
    Stream
      .resource(blockingExecutionContext)
      .flatMap(blockingEC => stream(blockingEC))
      .compile
      .toList
      .unsafeRunSync()
  }
}
