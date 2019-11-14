package eu.radusw.services

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import cats.implicits._
import courier._
import eu.radusw.AppConfig.Config
import eu.radusw.model.{Error, GiftPair, Person}
import eu.radusw.util.MonixExtensions.RichTask
import javax.mail.internet.InternetAddress
import monix.eval.Task
import monix.execution.Scheduler

import scala.concurrent.duration._
import scala.language.higherKinds
import scala.util.{Failure, Success}

trait MailService[F[_]] {
  def sendEmails(pairs: List[GiftPair]): F[Either[Error, List[Person]]]
}

final class MailServiceInterpreter(
  config: Config
)(
  implicit
  system: ActorSystem,
  scheduler: Scheduler
) extends MailService[Task] {

  implicit val log: LoggingAdapter = Logging(system, this.getClass)

  private val mailer: Mailer = Mailer(
    config.smtp.host,
    config.smtp.port
  )
    .auth(true)
    .as(config.smtp.user, config.smtp.password)
    .startTls(true)()

  override def sendEmails(pairs: List[GiftPair]): Task[Either[Error, List[Person]]] = {
    pairs
      .parTraverse(sendEmail)
      .map(_.sequence.leftMap(_ => Error("Santa messed up!")))
  }

  private def sendEmail(giftPair: GiftPair): Task[Either[Error, Person]] = Task
    .deferFuture {
      val htmlMessage = html.mail(s"${giftPair.giftRecipient.name}")
      mailer {
        Envelope.from(InternetAddress.parse(config.smtp.from).head)
          .to(InternetAddress.parse(giftPair.giftMaker.email).head)
          .subject("Secret Santa")
          .content(Multipart().html(htmlMessage.body))
      }.map(_ => giftPair.giftMaker)
    }
    .retryBackoff(1, 2.seconds)
    .materialize
    .map {
      case Success(giftMaker) =>
        log.info(s"Email has been sent to: ${giftPair.giftMaker.email}")
        Right(giftMaker)

      case Failure(exception) =>
        val err = Error(s"Could not send email to ${giftPair.giftMaker.email}. Error: ${exception.getMessage}")
        log.error(err.msg)
        Left(err)
    }
}
