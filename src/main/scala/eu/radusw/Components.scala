package eu.radusw

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import api._
import eu.radusw.services._
import monix.eval.Task
import monix.execution.Scheduler
import monix.execution.schedulers.SchedulerService

class Components(config: AppConfig.Config) {
  implicit val clientSystem: ActorSystem = ActorSystem("client")
  implicit val clientMat: ActorMaterializer = ActorMaterializer()

  implicit val blockingOpsScheduler: SchedulerService = Scheduler.io()

  val mailService: MailService[Task] = new MailServiceInterpreter(config)
  val santaService: SantaService[Task] = new SantaServiceInterpreter(mailService)

  val versionApi = new VersionApi()
  val mainApi = new MainApi(santaService)

  val frontendResource = new FrontendResource()
}
