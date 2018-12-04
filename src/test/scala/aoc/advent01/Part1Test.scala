package aoc.advent01

import aoc.testutil._
import aoc.util.readInput
import cats.data.Validated.Valid
import cats.effect.IO
import org.scalatest.{DiagrammedAssertions, FunSuiteLike}

class Part1Test extends FunSuiteLike with DiagrammedAssertions {

  test("site input") {
    val actual = runStream { blockingEC =>
      MainPart1.process(
        readInput[IO]("src/main/resources/01-input.txt", blockingEC)
      )
    }

    assert(actual === List(Valid(529)))
  }
}
