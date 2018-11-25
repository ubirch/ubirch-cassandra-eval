package com.ubirch.services

import com.datastax.driver.core.{ Cluster, PoolingOptions }
import javax.inject._

trait ClusterService {
  val poolingOptions: PoolingOptions
  val cluster: Cluster
}

@Singleton
class DefaultClusterService extends ClusterService {

  override val poolingOptions = new PoolingOptions

  override val cluster = Cluster.builder
    .addContactPoint("127.0.0.1")
    .withPort(9042)
    .withPoolingOptions(poolingOptions)
    .build

}
