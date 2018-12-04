package aoc.advent01

import aoc.testutil._
import aoc.util.readInput
import cats.data.Validated.Valid
import cats.effect.{ContextShift, IO}
import org.scalatest.{DiagrammedAssertions, FunSuiteLike}

import scala.concurrent.ExecutionContext

class Part2Test extends FunSuiteLike with DiagrammedAssertions {
  private implicit val contextShift: ContextShift[IO] =
    IO.contextShift(ExecutionContext.global)

  test("site input") {
    val actual = runStream { blockingEC =>
      MainPart2.process(
        readInput[IO]("src/main/resources/01-input.txt", blockingEC)
      )
    }

    assert(actual === List(Valid(464)))
  }
}
