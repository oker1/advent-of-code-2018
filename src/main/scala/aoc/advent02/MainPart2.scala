package aoc.advent02

import aoc.util._
import cats.effect._
import cats.implicits._
import fs2._

object MainPart2 extends IOApp {
  def process(source: Stream[IO, String]): Stream[IO, String] =
    source
      .map(line => List(line.trim))
      .foldMonoid
      .head
      .flatMap(
        lines =>
          source
            .map(line => (line.trim, lines))
      )
      .flatMap {
        case (line, lines) =>
          Stream(
            lines
              .find(_.zip(line).count(x => x._1 =!= x._2) === 1)
              .map(foundLine => (line, foundLine))
              .toSeq: _*
          )
      }
      .head
      .map {
        case (s1, s2) =>
          new String(
            s1.zip(s2).filter(chars => chars._1 === chars._2).unzip._1.toArray
          )
      }

  val stream: Stream[IO, Unit] =
    Stream.resource(blockingExecutionContext).flatMap { blockingEC =>
      process(readInput[IO]("src/main/resources/02-input.txt", blockingEC))
        .map(_.toString)
        .to(Sink.showLinesStdOut)
    }

  def run(args: List[String]): IO[ExitCode] =
    stream.compile.drain.map(_ => ExitCode.Success)
}
