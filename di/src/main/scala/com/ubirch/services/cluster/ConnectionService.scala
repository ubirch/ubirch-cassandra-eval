package com.ubirch.services.cluster

import com.ubirch.services.ClusterService
import io.getquill.{ CassandraAsyncContext, NamingStrategy, SnakeCase }
import javax.inject._

trait ConnectionService {
  type N <: NamingStrategy
  val context: CassandraAsyncContext[N]

}

@Singleton
class DefaultConnectionService @Inject() (clusterService: ClusterService) extends ConnectionService {

  override type N = SnakeCase.type

  override val context =
    new CassandraAsyncContext(
      SnakeCase,
      clusterService.cluster,
      "db",
      1000)

}