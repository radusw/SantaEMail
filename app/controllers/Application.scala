package controllers

import play.api._
import play.api.mvc._
import config.ApplicationContextHolder
import services.SantaService
import services.Person
import services.Mailer

object Application extends Controller with ServiceProvider[SantaService] {

  def index = Action {
    val result =
      service.formPairsForSecretSanta(List(Person("a", "A"), Person("b", "B"), Person("c", "C")))
    Mailer.sendSantaEmail(Person("gancea@eloquentix.com", "A"), Person("gancea@eloquentix.com", "B"))
    Ok(views.html.index(result.toString))
  }

}
