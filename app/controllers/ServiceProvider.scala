package controllers

import config.ApplicationContextHolder
import play.api.mvc.Controller

trait ServiceProvider[T] {
  self: Controller =>

  def service(implicit m: Manifest[T]): T =
    appContext.getBean(m.runtimeClass).asInstanceOf[T]

  lazy val appContext = ApplicationContextHolder.ctx
}
