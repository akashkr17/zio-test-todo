package edu.knoldus
import java.io.IOException

import Fib0._
import edu.knoldus.fiber.Party._
import zio._
import zio.test.Assertion.{equalTo, isTrue}
import zio.test.TestAspect.timeout
import zio.test.{DefaultRunnableSpec, Live, Spec, TestFailure, TestSuccess, assert, suite, test}
object Fib0 {
  def getIO: UIO[Unit] = IO.unit
  def getFiber: Fiber.Synthetic[Nothing, Unit] = Fiber.unit

}
object FibreSpec extends DefaultRunnableSpec {

  def spec: Spec[Live, TestFailure[IOException], TestSuccess] = suite("DanceFloorSpec")(
    test("Shows how to dance with Fibers") {
      for {
        out <- Live.live(party)
      } yield assert(out)(isTrue)
    } @@ timeout(15.seconds) ,
    test("Fib0 works") {
      for {
        fib <- getIO.fork
        out <- fib.await
      } yield assert(out)(equalTo(Exit.unit))
    }
  )
}
