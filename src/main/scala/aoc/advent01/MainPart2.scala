package aoc.advent01

import aoc.util._
import cats.data.Validated.{Invalid, Valid}
import cats.data.{NonEmptyChain, Validated}
import cats.effect._
import cats.implicits._
import cats.kernel.Monoid
import fs2._

object MainPart2 extends IOApp {
  case class Acc(frequency: Int, reached: Set[Int])
  object Acc {
    implicit val monoid: Monoid[Acc] = new Monoid[Acc] {
      override def empty: Acc = Acc(0, Set())
      override def combine(x: Acc, y: Acc): Acc =
        Acc(
          x.frequency.combine(y.frequency),
          x.reached.combine(Set(x.frequency))
        )
    }
  }

  def process(
    source: Stream[IO, String]
  ): Stream[IO, Validated[NonEmptyChain[Throwable], Int]] = {
    source.repeat
      .map(
        line => Validated.catchNonFatal(line.toInt).leftMap(NonEmptyChain.one)
      )
      .mapAccumulate(Acc(0, Set()).validNec[Throwable]) {
        (_, _).mapN { (acc, change) =>
          Acc(
            acc.frequency.combine(change),
            acc.reached.combine(Set(acc.frequency))
          )
        } -> ()
      }
      .map(_._1)
      .filter {
        case Valid(acc)    => acc.reached.contains(acc.frequency)
        case Invalid(errs) => true
      }
      .take(1)
      .map(_.map(_.frequency))
  }

  val converter: Stream[IO, Unit] =
    Stream.resource(blockingExecutionContext).flatMap { blockingEC =>
      process(readInput[IO]("src/main/resources/01-input.txt", blockingEC))
        .map(_.toString)
        .to(Sink.showLinesStdOut)
    }

  def run(args: List[String]): IO[ExitCode] =
    converter.compile.drain.map(_ => ExitCode.Success)
}
