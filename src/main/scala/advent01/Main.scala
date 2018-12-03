package advent01

import cats.effect._
import cats.implicits._
import fs2._
import java.nio.file.Paths
import java.util.concurrent.Executors

import scala.concurrent.ExecutionContext

object Main extends IOApp {
  private val blockingExecutionContext =
    Resource.make(
      IO(ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2)))
    )(ec => IO(ec.shutdown()))

  val converter: Stream[IO, Unit] =
    Stream.resource(blockingExecutionContext).flatMap { blockingEC =>
      io.file
        .readAll[IO](
          Paths.get("src/main/resources/01-input.txt"),
          blockingEC,
          4096
        )
        .through(text.utf8Decode)
        .through(text.lines)
        .filter(s => !s.trim.isEmpty)
        .map(line => line.toInt)
        .foldMonoid
        .flatMap(int => Stream(int.toString.getBytes: _*))
        .to(io.stdout(blockingEC))
    }

  def run(args: List[String]): IO[ExitCode] =
    converter.compile.drain.map(_ => ExitCode.Success)
}
