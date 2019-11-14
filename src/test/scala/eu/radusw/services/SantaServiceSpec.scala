package eu.radusw.services

import cats.Id
import cats.data.NonEmptyList
import cats.implicits._
import eu.radusw.model.{Error, GiftPair, Person}
import org.scalatest._

class SantaServiceSpec extends WordSpec with MustMatchers {

  "Santa Service" should {
    val mockMailService: MailService[Id] = (pairs: List[GiftPair]) => pairs.map(_.giftMaker).asRight[Error].pure[Id]
    val santaService: SantaService[Id] = new SantaService[Id] {
      override val mailService: MailService[Id] = mockMailService
    }

    "form valid pairs for secret santa" in {
      val persons = NonEmptyList.of(Person("a@a.com", "Ana"), Person("b@b.com", "Bogdan"), Person("c@c.com", "Carmen"))
      val pairs = santaService.formPairsForSecretSanta(persons)

      val txs = pairs.map(_.giftMaker)
      val rxs = pairs.map(_.giftRecipient)
      val everybodyGetsAndGiveSomething = persons.forall { p =>
        txs.contains(p) && rxs.contains(p)
      }
      (everybodyGetsAndGiveSomething && (pairs.size == persons.size)) mustBe true
    }
  }
}
