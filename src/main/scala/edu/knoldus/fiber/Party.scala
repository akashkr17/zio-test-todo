package edu.knoldus.fiber

import java.io.IOException

import zio._
import zio.Clock._
import zio.Console._


object Party {
  val fibers    = 4
  val dancers   = 10L
  val danceTime = 100000L

  val party: ZIO[Clock with Console with Random, IOException, Boolean] = for {
    dancefloor <- S(dancers)
    _ <- ZIO.foreachParDiscard(1 to fibers) {
      i =>
        dancefloor.P *> // decrease semaphore counter
          // The block below represents a Monadic semantics and runs sequentially in a single fiber
          zio.Random.nextDouble.map(d => Duration.fromNanos((d * 100000).round)).flatMap { d =>
            printLine(s"$i checking my boots") *> // effect 1
              sleep(d) *> // effect 2
              printLine(s"$i dancing like it's 99") // effect 3
          } *>
          dancefloor.V
    }
  } yield true

}
