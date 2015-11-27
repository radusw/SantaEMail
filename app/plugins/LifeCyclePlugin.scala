package plugins

import java.util.TimeZone
import play.api.{Logger, Plugin}
import config.{ AppConfig, ApplicationContextHolder }

/**
 * Provides application lifecycle events
 */
class LifeCyclePlugin(app: play.Application) extends Plugin {

  lazy val ctx = ApplicationContextHolder.ctx

  /**
   * Application start-up callback
   */
  override def onStart(): Unit = {
    Logger.info("Starting up Santa Service")
    setApplicationTimeZone("UTC")

    ctx.synchronized {
      if (!ctx.isActive) {
        Logger.info("Booting up Application Context")
        ctx.register(configurationClass)
        ctx.refresh()
        ctx.start()
      }
    }
  }

  protected def configurationClass: Class[_] = 
    classOf[AppConfig]

  /**
   * Application shut-down callback
   */
  override def onStop(): Unit = {
    Logger.info("Shutting down Santa Service")
    ctx.synchronized {
      ctx.stop()
    }
    super.onStop()
  }

  private def setApplicationTimeZone(timeZone:String): Unit = {
    Logger.info(s"Setting application timezone to [$timeZone]")
    System.setProperty("user.timezone", timeZone)
    TimeZone.setDefault(TimeZone.getTimeZone(timeZone))
  }

}
