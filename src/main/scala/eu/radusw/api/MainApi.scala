package eu.radusw.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.data.NonEmptyList
import eu.radusw.model.Person
import eu.radusw.services.SantaService
import monix.eval.Task
import monix.execution.Scheduler

final class MainApi(
  santaService: SantaService[Task]
)(
  implicit
  scheduler: Scheduler
) {

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

  val route: Route = {
    post {
      pathPrefix("run") {
        pathEndOrSingleSlash {
          entity(as[NonEmptyList[Person]]) { input =>
            complete(santaService.sendSecretSantaEmails(input).runToFuture)
          }
        }
      }
    }
  }
}
