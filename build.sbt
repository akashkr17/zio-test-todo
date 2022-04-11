name := "zio-test-todo"

version := "0.1"

scalaVersion := "2.13.8"

val zioVersion = "2.0.0-RC2"
libraryDependencies ++= Seq(
  "dev.zio" %% "zio"              % zioVersion,
  "dev.zio" %% "zio-streams"      % zioVersion,
  "dev.zio" %% "zio-test"         % zioVersion % "test",
  "dev.zio" %% "zio-test-sbt"     % zioVersion % "test"
)