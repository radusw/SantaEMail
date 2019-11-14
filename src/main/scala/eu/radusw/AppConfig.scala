package eu.radusw

import pureconfig.generic.auto._
import pureconfig.loadConfig

class AppConfig {
  import AppConfig._

  def config: Config = loadConfig[Config] match {
    case Right(conf) =>
      conf

    case Left(err) =>
      Console.err.println(err.toList)
      throw new Exception(err.head.description)
  }
}
object AppConfig {
  final case class Http(interface: String, port: Int)
  final case class Smtp(host: String, port: Int, ssl: Boolean, user: String, password: String, from: String)
  final case class Config(http: Http, smtp: Smtp, logToFile: Boolean)
}
