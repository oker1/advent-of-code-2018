package aoc.advent02

import aoc.util._
import cats.effect._
import cats.implicits._
import fs2._

object MainPart1 extends IOApp {
  private def letterCount(in: String) =
    in.trim.toArray
      .groupBy(identity)
      .values
      .map(_.length)
      .toSet

  def process(source: Stream[IO, String]): Stream[IO, Int] =
    source
      .map(letterCount)
      .fold((0, 0)) { (acc, set) =>
        val two = if (set.contains(2)) 1 else 0
        val three = if (set.contains(3)) 1 else 0
        acc.combine((two, three))
      }
      .map { case (twos, threes) => twos * threes }

  val stream: Stream[IO, Unit] =
    Stream.resource(blockingExecutionContext).flatMap { blockingEC =>
      process(readInput[IO]("src/main/resources/02-input.txt", blockingEC))
        .map(_.toString)
        .to(Sink.showLinesStdOut)
    }

  def run(args: List[String]): IO[ExitCode] =
    stream.compile.drain.map(_ => ExitCode.Success)
}
