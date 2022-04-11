package edu.knoldus

import zio.test._
import zio.test.Assertion.{isRight,isLeft,isSome,equalTo, hasField,isGreaterThanEqualTo,not}
import Assertion.isGreaterThan
import zio.Clock.nanoTime
import zio.test.TestRandom._
object ZioTestSpec extends DefaultRunnableSpec {
  final case class Address(country:String, city:String)
  final case class User(name:String, age:Int, address: Address)
  override def spec: ZSpec[TestEnvironment, Any] = suite("clock") (
    test("time is non-zero") {
      assertM(Live.live(nanoTime))(isGreaterThan(0L))
    },
      test("Check assertions") {
      assert(Left(Some(2)))(isLeft(isSome(equalTo(2))))
    },
    test("Check assertions Right") {
      assert(Right(Some(2)))(isRight(isSome(equalTo(2))))
    },
    test("Rich checking") {
      assert(
        User("Jonny", 26, Address("Denmark", "Copenhagen"))
      )(
        hasField("age", (u:User) => u.age, isGreaterThanEqualTo(18)) &&
          hasField("country", (u:User) => u.address.country, not(equalTo("USA")))
      )
    }



  )
}
