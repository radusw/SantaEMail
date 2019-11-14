package eu.radusw

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.util.Try

object Main extends App {
  Try(args(0)).foreach(System.setProperty("config.file", _))
  val configuration: AppConfig.Config = new AppConfig().config

  if (!configuration.logToFile) {
    System.setProperty("logback.configurationFile", "logback.stdout.xml")
  }

  val components = new Components(configuration)
  val routes =
    pathPrefix("app") {
      components.frontendResource.route
    } ~
      pathPrefix("api") {
        components.versionApi.route ~
          components.mainApi.route
      }

  implicit val appSystem: ActorSystem = ActorSystem("app")
  implicit val appMat: ActorMaterializer = ActorMaterializer()
  private val log = Logging(appSystem, this.getClass)

  Http().bindAndHandle(Route.seal(routes), configuration.http.interface, configuration.http.port)
  log.info(s"Server up at $configuration")
}
