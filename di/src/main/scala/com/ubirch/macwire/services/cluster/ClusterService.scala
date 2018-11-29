package com.ubirch.macwire.services.cluster

import com.datastax.driver.core.{ Cluster, PoolingOptions }
import com.ubirch.macwire.services.config.ConfigModule

import scala.collection.JavaConverters._

private[cluster] object ClusterServiceProvider extends ConfigModule {

  val contactPoints: List[String] = config.getStringList("eventLog.cluster.contactPoints").asScala.toList
  val port: Int = config.getInt("eventLog.cluster.port")

  val poolingOptions = new PoolingOptions

  val cluster = Cluster.builder
    .addContactPoints(contactPoints: _*)
    .withPort(port)
    .withPoolingOptions(poolingOptions)
    .build

}

private[cluster] class ClusterServiceProvider {
  def cluster = ClusterServiceProvider.cluster
}

trait ClusterServiceModule {

  import com.softwaremill.macwire._

  private def clusterProvider = wire[ClusterServiceProvider]
  def cluster: Cluster = clusterProvider.cluster
}
