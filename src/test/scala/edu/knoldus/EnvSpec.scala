package edu.knoldus


import edu.knoldus.env.Common.{UserID, UserProfile}
import edu.knoldus.env.TestService
import zio.test.Assertion.equalTo
import zio.test._

object EnvSpec extends DefaultRunnableSpec {
  def spec: Spec[Any, TestFailure[Throwable], TestSuccess] = suite("EnvSpec")(
    test("access to Mock DB") {
      val user12 = UserID(12)
      val prof12 = UserProfile(user12)

      for {
        _  <- TestService.setTestData(Map(user12 -> prof12))
        p0 <- TestService.lookup(user12)
      } yield assert(p0)(equalTo(prof12))
    },

    test("update  DB") {
      val user12 = UserID(12)
      val prof12 = UserProfile(user12)

      val user13 = UserID(13)
      val prof13 = UserProfile(user13)
      for {
        _  <- TestService.setTestData(Map(user12 -> prof12))
        _ <- TestService.update(user13,prof13)
        p0 <- TestService.lookup(user13)
      } yield assert(p0)(equalTo(prof13))
    }
  )
}

