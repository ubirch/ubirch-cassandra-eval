package com.ubirch.macwire.services.cluster

import com.ubirch.macwire.services.config.ConfigModule
import com.ubirch.macwire.services.lifeCycle.LifecycleModule
import io.getquill.{ CassandraAsyncContext, SnakeCase }

import scala.concurrent.Future

private[cluster] object ConnectionServiceProvider
  extends ClusterServiceModule
  with LifecycleModule
  with ConfigModule {

  val keyspace: String = config.getString("eventLog.cluster.keyspace")
  val preparedStatementCacheSize: Int = config.getInt("eventLog.cluster.preparedStatementCacheSize")

  type N = SnakeCase.type

  val context =
    new CassandraAsyncContext(
      SnakeCase,
      cluster,
      keyspace,
      preparedStatementCacheSize)

  lifecycle.addStopHook { () =>
    Future.successful(context.close())
  }

}

private[cluster] class ConnectionServiceProvider {
  def context = ConnectionServiceProvider.context
}

trait ConnectionServiceModule {

  import com.softwaremill.macwire._

  private def contextProvider = wire[ConnectionServiceProvider]

  def context = contextProvider.context

}

