package utils

import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.duration.FiniteDuration
import scala.jdk.DurationConverters._


object AppConfig {

  private val config: Config = ConfigFactory.load()

  val lookupInterval: FiniteDuration = config.getDuration("lookup-interval").toScala
  val telegramChannel: String = config.getString("telegram.channel-name")
  val telegramApiToken: String = config.getString("telegram.api-token")

}
