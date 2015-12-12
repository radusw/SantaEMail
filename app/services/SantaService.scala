package services

import org.springframework.stereotype.Service
import play.api.libs.json._
import play.api.libs.functional.syntax._

trait SantaService {
  def formPairsForSecretSanta(persons: Seq[Person]): Seq[(Person, Person)]
  def sendEmails(pairs: Seq[(Person, Person)])
}

@Service
class SantaServiceImpl extends SantaService {

  override def formPairsForSecretSanta(persons: Seq[Person]) = {
    import scala.util.Random

    @scala.annotation.tailrec
    def loop(acc: Seq[(Person, Person)], txs: Seq[Person], rxs: Seq[Person]): Seq[(Person, Person)] =
      (txs, rxs) match {
        case (a :: as, b :: bs) if (txs.size == 2 && (txs intersect rxs).isDefinedAt(0)) =>
          val tx = (txs intersect rxs).head
          val rx = (rxs filterNot { _ == tx }).head
          loop(acc :+ ((tx, rx)), txs filterNot { _ == tx}, rxs filterNot { _ == rx})
	      case (a :: as, b :: bs) =>
		      val tx = Random.shuffle(txs).head
		      val rx = Random.shuffle(rxs filterNot {_ == tx}).headOption.getOrElse(tx)
		      loop(acc :+ ((tx, rx)), txs filterNot { _ == tx}, rxs filterNot { _ == rx})
		    case (Nil, Nil) =>
		      acc
	    }
    val pairs = loop(Nil, persons, persons)

    pairs
  }

  override def sendEmails(pairs: Seq[(Person, Person)]) {
    for(p <- pairs) Mailer.sendSantaEmail(p._1, p._2)
  }

}


case class Person(email: String, name: String)

object Person {
  implicit val personReads = Json.reads[Person]
  implicit val personWrites = Json.writes[Person]
}