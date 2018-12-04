package aoc.advent01

import aoc.util._
import cats.data.{NonEmptyChain, Validated}
import cats.effect._
import cats.implicits._
import fs2._

object MainPart1 extends IOApp {
  def process(
    source: Stream[IO, String]
  ): Stream[IO, Validated[NonEmptyChain[Throwable], Int]] =
    source
      .map(
        line => Validated.catchNonFatal(line.toInt).leftMap(NonEmptyChain.one)
      )
      .foldMonoid

  val converter: Stream[IO, Unit] =
    Stream.resource(blockingExecutionContext).flatMap { blockingEC =>
      process(readInput[IO]("src/main/resources/01-input.txt", blockingEC))
        .map(_.toString)
        .to(Sink.showLinesStdOut)
    }

  def run(args: List[String]): IO[ExitCode] =
    converter.compile.drain.map(_ => ExitCode.Success)
}
