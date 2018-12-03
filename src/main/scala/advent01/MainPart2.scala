package advent01

import java.nio.file.Paths
import java.util.concurrent.Executors

import cats.effect._
import cats.implicits._
import cats.kernel.Monoid
import fs2._

import scala.concurrent.ExecutionContext

object MainPart2 extends IOApp {
  private val blockingExecutionContext =
    Resource.make(
      IO(ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2)))
    )(ec => IO(ec.shutdown()))

  case class Acc(frequency: Int, reachedFrequencies: Set[Int])

  val converter: Stream[IO, Unit] =
    Stream.resource(blockingExecutionContext).flatMap { blockingEC =>
      io.file
        .readAll[IO](
          Paths.get("src/main/resources/01-input.txt"),
          blockingEC,
          4096
        )
        .repeat
        .through(text.utf8Decode)
        .through(text.lines)
        .filter(s => !s.trim.isEmpty)
        .map(line => line.toInt)
        .mapAccumulate(Acc(0, Set())) { (acc, change) =>
          val nextFrequency = acc.frequency.combine(change)
          acc.copy(
            nextFrequency,
            acc.reachedFrequencies.combine(Set(acc.frequency))
          ) -> change
        }
        .map(_._1)
        .filter(acc => acc.reachedFrequencies.contains(acc.frequency))
        .take(1)
        .map(_.frequency)
        .flatMap(int => Stream(int.toString.getBytes: _*))
        .to(io.stdout(blockingEC))
    }

  def run(args: List[String]): IO[ExitCode] =
    converter.compile.drain.map(_ => ExitCode.Success)
}
