val slickVersion = "3.3.3"
val postgresqlVersion = "42.2.18"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """play-first""",
    organization := "ru.juliomoralez",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.13.1",
    libraryDependencies ++= Seq(
      guice,
      "com.typesafe.slick" %% "slick" % slickVersion,
      "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
      "org.postgresql" % "postgresql" % postgresqlVersion,
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
    ),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings"
    )
  )
