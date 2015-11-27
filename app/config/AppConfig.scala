package config

import java.util.Properties
import javax.sql.DataSource
import config.AppConfig._
import play.api.Play
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = Array(
  "config",
  "services"))
class AppConfig {
  
}

object AppConfig {
  import play.api.Play.current

  lazy val smtpHost = getString("smtp.host")
  lazy val mailFrom = getString("smtp.from")

  val connectionPoolSize = Runtime.getRuntime.availableProcessors()*2 + 1

  private val configuration = 
    Play.configuration

  private def getString(key: String) =
    configuration.getString(key) getOrElse error

  private def getInt(key: String) =
    configuration.getInt(key) getOrElse error

  private def getBoolean(key: String) =
    configuration.getBoolean(key) getOrElse error

  private def error =
    throw new IllegalStateException("env property not found")
}
