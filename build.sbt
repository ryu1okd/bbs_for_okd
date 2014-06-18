import play.Project._

name := "bbs_for_okd"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  "com.typesafe.slick" %% "slick" % "2.0.2",
  "joda-time" % "joda-time" % "2.3",
  "org.joda" % "joda-convert" % "1.5",
  "com.github.tototoshi" % "slick-joda-mapper_2.10" % "1.1.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.play" % "play-slick_2.10" % "0.6.0.1" withSources
)     

play.Project.playScalaSettings
