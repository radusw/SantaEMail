package eu.radusw.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import eu.radusw.model.Person
import eu.radusw.services.{MailService, SantaService}
import monix.eval.Task
import monix.execution.Scheduler

final class MainApi(
  santaService: SantaService[Task],
  mailService: MailService[Task],
)(
  implicit
  scheduler: Scheduler
) {

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

  val route: Route = {
    post {
      pathPrefix("run") {
        pathEndOrSingleSlash {
          entity(as[List[Person]]) { input =>
            val program = for {
              pairs <- santaService.formPairsForSecretSanta(input)
              result <- mailService.sendEmails(pairs)
            } yield result
            complete(program.runToFuture)
          }
        }
      }
    }
  }
}
