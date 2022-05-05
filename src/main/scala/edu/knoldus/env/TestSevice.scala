package edu.knoldus.env

import zio.Task
import edu.knoldus.env.Common._

class TestService extends Database.Service {
  private var map: Map[UserID, UserProfile] = Map()

  def setTestData(map0: Map[UserID, UserProfile]): Task[Unit] =
    Task { map = map0 }

  def getTestData: Task[Map[UserID, UserProfile]] =
    Task(map)

  def lookup(id: UserID): Task[UserProfile] =
    Task(map(id))

  def update(id: UserID, profile: UserProfile): Task[Unit] =
    Task.attempt { map = map + (id -> profile) }
}

object TestService extends TestService

trait TestDatabase extends Database {
  val database: TestService = new TestService
}
object TestDatabase extends TestDatabase
