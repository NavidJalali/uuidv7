ThisBuild / organization := "io.navidjalali"
ThisBuild / organizationName := "navidjalali"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.4.2"

val Versions = new {
  val cats = "2.12.0"
  val catsEffect = "3.5.4"
  val zio = "2.1.3"
  val zioCatsInterop = "23.1.0.2"
}

lazy val core = (project in file("core"))
  .settings(
    name := "uuidv7-core",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % Versions.cats
    )
  )

lazy val uuidv7CatsEffect = (project in file("uuidv7-cats-effect"))
  .dependsOn(core)
  .settings(
    name := "uuidv7-cats-effect",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % Versions.catsEffect
    ),
    scalacOptions ++= Seq(
      "-Ykind-projector:underscores"
    )
  )

lazy val uuidv7Zio = (project in file("uuidv7-zio"))
  .dependsOn(core)
  .settings(
    name := "uuidv7-zio",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % Versions.zio,
      "dev.zio" %% "zio-interop-cats" % Versions.zioCatsInterop
    ),
    scalacOptions ++= Seq(
      "-Ykind-projector:underscores"
    )
  )
