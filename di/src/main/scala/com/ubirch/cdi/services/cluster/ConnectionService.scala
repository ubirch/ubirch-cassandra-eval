package com.ubirch.cdi.services.cluster

import com.ubirch.cdi.services.config.DefaultConfig
import com.ubirch.cdi.services.lifeCycle.DefaultLifecycle
import io.getquill.{ CassandraAsyncContext, SnakeCase }

import scala.concurrent.Future

private[cluster] trait ConnectionServiceConfig {
  val keyspace: String
  val preparedStatementCacheSize: Int
}

private[cluster] object ConnectionServiceProvider
  extends DefaultClusterService
  with DefaultLifecycle
  with DefaultConfig
  with ConnectionServiceConfig {

  val keyspace: String = config.getString("eventLog.cluster.keyspace")
  val preparedStatementCacheSize: Int = config.getInt("eventLog.cluster.preparedStatementCacheSize")

  type N = SnakeCase.type

  val context =
    new CassandraAsyncContext(
      SnakeCase,
      cluster,
      keyspace,
      preparedStatementCacheSize)

  addStopHook { () =>
    Future.successful(context.close())
  }

}

trait ConnectionService {
  def context: CassandraAsyncContext[_]

}

trait DefaultConnectionService extends ConnectionService {
  def context = ConnectionServiceProvider.context
}

