package com.ubirch.cdi.services.config

import com.typesafe.config.{ Config, ConfigFactory }

private[config] object ConfigProvider {

  val conf = ConfigFactory.load()

}

trait ConfigBase {
  def config: Config
}

trait DefaultConfig extends ConfigBase {
  def config: Config = ConfigProvider.conf
}
