package edu.knoldus.env

import zio.{ Task, ZIO }

object Common {
  final case class UserID(n: Int)
  final case class UserProfile(id: UserID)

}

object Database {
  import Common._

  trait Service {
    def lookup(id: UserID): Task[UserProfile]
    def update(id: UserID, profile: UserProfile): Task[Unit]
  }
}
trait Database {
  def database: Database.Service
}

object db {
  import Common._

  def lookup(id: UserID): ZIO[Database, Throwable, UserProfile] =
    ZIO.environmentWithZIO(_.get.database.lookup(id))

  def update(id: UserID, profile: UserProfile): ZIO[Database, Throwable, Unit] =
    ZIO.environmentWithZIO(data => data.get.database.update(id,profile))
}
