package com.ubirch.cdi.services.cluster

import com.datastax.driver.core.{ Cluster, PoolingOptions }
import com.ubirch.cdi.services.config.DefaultConfig

import scala.collection.JavaConverters._

private[cluster] trait ClusterConfigs {
  def contactPoints: List[String]
  def port: Int
}

private[cluster] object ClusterServiceProvider
  extends DefaultConfig
  with ClusterConfigs {

  val contactPoints: List[String] = config.getStringList("eventLog.cluster.contactPoints").asScala.toList
  val port: Int = config.getInt("eventLog.cluster.port")

  val poolingOptions = new PoolingOptions

  val cluster = Cluster.builder
    .addContactPoints(contactPoints: _*)
    .withPort(port)
    .withPoolingOptions(poolingOptions)
    .build

}

trait ClusterServiceBase {
  def poolingOptions: PoolingOptions
  def cluster: Cluster
}

trait DefaultClusterService extends ClusterServiceBase {
  def poolingOptions: PoolingOptions = ClusterServiceProvider.poolingOptions
  def cluster: Cluster = ClusterServiceProvider.cluster
}
