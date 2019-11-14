package eu.radusw.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class VersionApi() {
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

  val route: Route = pathPrefix("version") {
    pathEndOrSingleSlash {
      get {
        complete(io.circe.parser.parse(BuildInfo.toJson))
      }
    }
  }
}
