package com.ubirch.macwire.services.config

import com.typesafe.config.{ Config, ConfigFactory }

private[config] object ConfigProvider {

  val conf = ConfigFactory.load()

}

private[config] class ConfigProvider {
  def conf = ConfigProvider.conf
}

trait ConfigModule {
  import com.softwaremill.macwire._

  private def configProvider = wire[ConfigProvider]
  def config: Config = configProvider.conf

}
