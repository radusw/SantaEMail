package config

import java.io.File

import com.typesafe.config.ConfigFactory

import play.api.Configuration
import play.api.GlobalSettings
import play.api.Mode

object Global extends GlobalSettings {

  /**
   * Override play configuration loading to allow multiple environments
   */
  override def onLoadConfig(
      config: Configuration,
      path: File,
      classLoader: ClassLoader,
      mode: Mode.Mode): Configuration = {
    val modeSpecificConfig = config ++ Configuration(ConfigFactory.load("mail.conf"))
    super.onLoadConfig(modeSpecificConfig, path, classLoader, mode)
  }
}