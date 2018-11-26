package com.ubirch.services.cluster

import com.typesafe.config.Config
import com.ubirch.services.ClusterService
import com.ubirch.services.lifeCycle.Lifecycle
import io.getquill.{ CassandraAsyncContext, NamingStrategy, SnakeCase }
import javax.inject._

import scala.concurrent.Future

trait ConnectionServiceConfig {
  val keyspace: String
  val preparedStatementCacheSize: Int
}

trait ConnectionService extends ConnectionServiceConfig {
  type N <: NamingStrategy
  val context: CassandraAsyncContext[N]

}

@Singleton
class DefaultConnectionService @Inject() (clusterService: ClusterService, config: Config, lifecycle: Lifecycle)
  extends ConnectionService {

  val keyspace: String = config.getString("eventLog.cluster.keyspace")
  val preparedStatementCacheSize: Int = config.getInt("eventLog.cluster.preparedStatementCacheSize")

  type N = SnakeCase.type

  override val context =
    new CassandraAsyncContext(
      SnakeCase,
      clusterService.cluster,
      keyspace,
      preparedStatementCacheSize)

  lifecycle.addStopHook { () =>
    Future.successful(context.close())
  }

}