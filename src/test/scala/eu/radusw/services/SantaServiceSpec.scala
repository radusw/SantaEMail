package eu.radusw.services

import akka.actor.ActorSystem
import eu.radusw.model.Person
import monix.execution.Scheduler
import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class SantaServiceSpec extends WordSpec with MustMatchers with BeforeAndAfterAll {

  implicit private val system: ActorSystem = ActorSystem.apply("test")
  implicit val scheduler: Scheduler = Scheduler(global)

  "Santa Service" should {
    val service = new SantaServiceInterpreter

    "form valid pairs for secret santa" in {
      val persons = List(Person("a@a.com", "Ana"), Person("b@b.com", "Bogdan"), Person("c@c.com", "Carmen"))
      val pairs = service.formPairsForSecretSanta(persons).runSyncUnsafe()
      println(pairs.toString)

      val txs = pairs.map(_.giftMaker)
      val rxs = pairs.map(_.giftRecipient)
      val everybodyGetsAndGiveSomething = persons.forall { p =>
        txs.contains(p) && rxs.contains(p)
      }
      (everybodyGetsAndGiveSomething && (pairs.size == persons.size)) mustBe true
    }
  }

  override def afterAll(): Unit = {
    Await.result(system.terminate().map(_ => ()), Duration.Inf)
  }
}
