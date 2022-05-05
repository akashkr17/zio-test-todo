package edu.knoldus.fiber

import zio.{ IO, Ref, UIO }

sealed trait S {
  def P: UIO[Unit]
  def V: UIO[Unit]
}

object S {
  def apply(v: Long): UIO[S] =
    Ref.make(v).map { vref =>
      new S {
        def V: UIO[Unit] = vref.update(_ + 1).unit

        def P: UIO[Unit] =
          (vref.get.flatMap { v =>
            if (v < 0)
              IO.fail(())
            else
              vref.modify(v0 => if (v0 == v) (true, v - 1) else (false, v)).flatMap {
                case false => IO.fail(())
                case true  => IO.unit
              }
          } <> P)
      }
    }
}
