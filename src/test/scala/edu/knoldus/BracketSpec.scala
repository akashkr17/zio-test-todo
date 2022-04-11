package edu.knoldus

import java.io.{File, FileInputStream}
import java.nio.charset.StandardCharsets

import zio._
import zio.test.Assertion.{equalTo, succeeds}
import zio.test._

import scala.io.{BufferedSource, Source}

object BracketSpec extends DefaultRunnableSpec {
  def spec: Spec[Any, TestFailure[Throwable], TestSuccess] = suite("Bracket Spec")(
    test("String IO bracket") {
      for {
        str <- IO.succeed("Hello").acquireReleaseWith(_ => IO.succeed("Fail"))(_ => IO.succeed("Pass")).exit
        _   = println(str)
      } yield assert(str)(succeeds(equalTo("Pass")))

    },
    test("File access bracket") {
      for {
        file <- Task(new File("file.txt"))
        // len  = file.length
        string <-
          ZIO(Source.fromFile(file))
            .acquireReleaseWith(
              (s: BufferedSource) => URIO(s.close),
              s => ZIO(s.getLines().mkString)
            )
      } yield assert(string)(equalTo("hello"))
    }
  )
}