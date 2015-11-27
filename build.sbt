name := """SantaEMail"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"
val springVersion = "4.2.2.RELEASE"

libraryDependencies ++= Seq(
  ws,
  filters,
  javaCore,
  "org.springframework" % "spring-context" % springVersion withSources(),
  "org.springframework" % "spring-context-support" % springVersion withSources(),
  "org.springframework" % "spring-core" % springVersion withSources(),
  "org.scala-lang.modules" %% "scala-async" % "0.9.5",
  "com.typesafe.play" % "play-mailer_2.11" % "2.4.1",
  "org.scalatest" %% "scalatest" % "2.2.2" % "test",
  "org.scalatestplus" %% "play" % "1.2.0" % "test",
  "org.mockito" % "mockito-all" % "1.9.5" % "test"
)

resolvers ++= Seq(
  "JBoss repository" at "https://repository.jboss.org/nexus/content/repositories/",
  "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
  "Apache Snapshots" at "https://repository.apache.org/content/repositories/snapshots/",
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)
