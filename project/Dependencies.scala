import sbt._

object Dependencies {
  val akkaV      = "2.5.23"
  val akkaHttpV  = "10.1.10"
  val circeV     = "0.11.1"
  val akkaCirceV = "1.27.0"
  val scalaTestV = "3.0.1"
  val logbackV   = "1.2.3"
  val configV    = "1.3.4"
  val timeV      = "2.18.0"
  val monixV     = "3.1.0"
  val pureConfV  = "0.11.1"
  val couriesV   = "2.0.0"


  lazy val projectResolvers = Seq.empty
  lazy val dependencies = testDependencies ++ rootDependencies


  lazy val testDependencies = Seq (
    "org.scalatest"          %% "scalatest"             % scalaTestV % Test,
    "com.typesafe.akka"      %% "akka-http-testkit"     % akkaHttpV  % Test
  )

  lazy val rootDependencies = Seq(
    "com.github.daddykotex"  %% "courier"               % couriesV,
    "com.typesafe.akka"      %% "akka-http"             % akkaHttpV,
    "de.heikoseeberger"      %% "akka-http-circe"       % akkaCirceV,
    "io.monix"               %% "monix"                 % monixV,
    "io.circe"               %% "circe-core"            % circeV,
    "io.circe"               %% "circe-generic"         % circeV,
    "io.circe"               %% "circe-parser"          % circeV,
    "com.github.nscala-time" %% "nscala-time"           % timeV,
    "com.typesafe"            % "config"                % configV,
    "com.typesafe.akka"      %% "akka-slf4j"            % akkaV,
    "ch.qos.logback"          % "logback-classic"       % logbackV,
    "com.github.pureconfig"  %% "pureconfig"            % pureConfV
  )
}
