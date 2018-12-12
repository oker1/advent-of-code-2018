package aoc.advent02

import aoc.testutil._
import aoc.util.readInput
import cats.effect.IO
import org.scalatest.{DiagrammedAssertions, FunSuiteLike}

class Part1Test extends FunSuiteLike with DiagrammedAssertions {
  test("site input") {
    val actual = runStream { blockingEC =>
      MainPart1.process(
        readInput[IO]("src/main/resources/02-input.txt", blockingEC)
      )
    }

    assert(actual === List(8715))
  }
}
