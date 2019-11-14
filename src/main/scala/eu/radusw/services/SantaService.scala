package eu.radusw.services

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import eu.radusw.model.{GiftPair, Person}
import monix.eval.Task

import scala.language.higherKinds
import scala.util.Random

trait SantaService[F[_]] {
  def formPairsForSecretSanta(persons: List[Person]): F[List[GiftPair]]
}

final class SantaServiceInterpreter(
  implicit
  system: ActorSystem
) extends SantaService[Task] {

  implicit val log: LoggingAdapter = Logging(system, this.getClass)

  override def formPairsForSecretSanta(persons: List[Person]): Task[List[GiftPair]] = Task {
    @scala.annotation.tailrec
    def loop(
      acc: List[GiftPair],
      txs: List[Person],
      rxs: List[Person]
    ): List[GiftPair] = (txs, rxs) match {
      case (_ :: _, _ :: _) if txs.size == 2 && (txs intersect rxs).isDefinedAt(0) =>
          val tx = txs.intersect(rxs).head
          val rx = rxs.filterNot(_ == tx).head
          loop(
            acc :+ GiftPair(tx, rx),
            txs.filterNot(_ == tx),
            rxs.filterNot(_ == rx)
          )

        case (_ :: _, _ :: _) =>
          val tx = Random.shuffle(txs).head
          val rx = Random.shuffle(rxs.filterNot(_ == tx)).headOption.getOrElse(tx)
          loop(
            acc :+ GiftPair(tx, rx),
            txs.filterNot(_ == tx),
            rxs.filterNot(_ == rx)
          )

        case (Nil, Nil) =>
          acc
      }
    loop(Nil, persons, persons)
  }
}
