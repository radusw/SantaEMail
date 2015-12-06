package services

import play.api.Play._
import play.api.i18n.{Lang, Messages}
import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.mailer
import play.api.libs.mailer.{Attachment, Email}
import play.twirl.api.{Html, Txt}
import scala.concurrent.duration._

object Mailer {

  def sendSantaEmail(sender: Person, recipient: Person) {
    val msg = views.html.mail(s"${recipient.name}")
    sendEmail("Secret Santa", sender.email, (None, Some(msg)))
  }

  private def sendEmail(
    subject: String,
    recipient: String,
    body: (Option[Txt], Option[Html]),
    attachments: Seq[Attachment] = Seq.empty[Attachment]) = Akka.system.scheduler.scheduleOnce(1.seconds) {
      createMail(subject, recipient, body, attachments)
  }

  private def createMail(
      subject: String,
      recipient: String,
      body: (Option[Txt], Option[Html]),
      attachments: Seq[Attachment] = Seq.empty[Attachment]) {
    val mail = mailer.MailerPlugin
    val recipients = recipient.split(",").map(_.trim)

    val email = Email(
      subject,
      config.AppConfig.mailFrom,
      recipients,
      attachments = attachments,
      bodyText = Option(body._1.map(_.body).getOrElse("")),
      bodyHtml = Option(body._2.map(_.body).getOrElse(""))
    )

    mail.send(email)
  }
}