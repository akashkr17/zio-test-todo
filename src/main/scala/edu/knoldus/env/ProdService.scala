package edu.knoldus.env


import zio.Task
import edu.knoldus.env.Common._

trait DatabaseLive extends Database {
  def database: Database.Service =
    new Database.Service {
      def lookup(id: UserID): Task[UserProfile]                = ???
      def update(id: UserID, profile: UserProfile): Task[Unit] = ???
    }
}
object DatabaseLive extends DatabaseLive
