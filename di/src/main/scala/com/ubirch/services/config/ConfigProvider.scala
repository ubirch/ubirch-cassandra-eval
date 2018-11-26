package com.ubirch.services.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import javax.inject._

@Singleton
class ConfigProvider extends Provider[Config] {

  val conf = ConfigFactory.load()

  override def get() = conf

}
