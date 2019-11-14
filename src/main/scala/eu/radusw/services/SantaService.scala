package eu.radusw.services

import cats.{Applicative, Monad}
import cats.data.NonEmptyList
import cats.implicits._
import eu.radusw.model.{Error, GiftPair, Person}
import monix.eval.Task

import scala.language.higherKinds
import scala.util.Random

trait SantaService[F[_]] {
  def mailService: MailService[F]

  def formPairsForSecretSanta(
    persons: NonEmptyList[Person]
  )(
    implicit ev: Applicative[F]
  ): F[List[GiftPair]] = ev.pure {
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
    loop(Nil, persons.toList, persons.toList)
  }

  def sendSecretSantaEmails(
    persons: NonEmptyList[Person]
  )(
    implicit
    ev: Monad[F]
  ): F[Either[Error, List[Person]]] = {
    for {
      pairs <- formPairsForSecretSanta(persons)
      result <- mailService.sendEmails(pairs)
    } yield result
  }
}

final class SantaServiceInterpreter(
  override val mailService: MailService[Task]
) extends SantaService[Task]
