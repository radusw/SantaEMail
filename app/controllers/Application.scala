package controllers

import play.api._
import play.api.mvc._
import config.ApplicationContextHolder
import services.SantaService
import services.Person
import services.Mailer
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json._
import play.Logger

object Application extends Controller with ServiceProvider[SantaService] {

  def index = Action {
    Ok(views.html.index("Who's in?"))
  }

  def run = Action(parse.json) { implicit request => 
    val whosIn = request.body.asOpt[JsArray] map { data =>
      for {
        entry <- data.value.toSeq
        name <- (entry \ "name").asOpt[String]
        email <- (entry \ "email").asOpt[String]
      } yield Person(email = email, name = name)
    }

    val pairs = service.formPairsForSecretSanta(whosIn.getOrElse(Seq.empty[Person]))
    pairs foreach { p =>
      Mailer.sendSantaEmail(p._1, p._2)
    }

    Logger.info("emails sent :: " + pairs)
    Ok(Json.toJson(pairs map {_._1})) as JSON
  }

}