package com.ubirch.services.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import javax.inject._

import scala.collection.JavaConverters.asScalaBufferConverter

@Singleton
class ConfigProvider extends Provider[Config] {

  val conf = ConfigFactory.load()

  override def get() = conf

}

object ConfigProvider {
  def asScalaList[A](l: java.util.List[A]): Seq[A] = asScalaBufferConverter(l).asScala.toList
}
