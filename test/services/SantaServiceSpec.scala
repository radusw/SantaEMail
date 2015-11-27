package services

import play.Logger
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.OneAppPerSuite

class SantaServiceSpec extends PlaySpec with OneAppPerSuite {
  import config.ApplicationContextHolder.ctx

  "Santa Service" should {
    "form valid pairs for secret santa" in {
      val service = ctx.getBean(classOf[SantaService])
      
      val persons = List(Person("a@a.com", "Ana"), Person("b@b.com", "Bogdan"), Person("c@c.com", "Carmen"))
      val pairs = service.formPairsForSecretSanta(persons)
      Logger.info(pairs.toString)

      val txs = pairs map { _._1 }
      val rxs = pairs map { _._2 }

      val everybodyGetsAndGiveSomething = persons forall { p =>
        txs.contains(p)
        rxs.contains(p)
      }
      
      (everybodyGetsAndGiveSomething && (pairs.size == persons.size)) mustBe true
    }
  }

}