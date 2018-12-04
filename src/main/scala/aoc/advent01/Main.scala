package aoc.advent01

import aoc.util._
import cats.data.{NonEmptyChain, Validated}
import cats.effect._
import cats.implicits._
import fs2._

object Main extends IOApp {
  val converter: Stream[IO, Unit] =
    Stream.resource(blockingExecutionContext).flatMap { blockingEC =>
      readInput[IO]("src/main/resources/01-input.txt", blockingEC)
        .map(
          line => Validated.catchNonFatal(line.toInt).leftMap(NonEmptyChain.one)
        )
        .foldMonoid
        .flatMap(
          validated =>
            Stream
              .fromIterator[IO, Byte](validated.toString.getBytes.toIterator)
        )
        .to(io.stdout(blockingEC))
    }

  def run(args: List[String]): IO[ExitCode] =
    converter.compile.drain.map(_ => ExitCode.Success)
}
