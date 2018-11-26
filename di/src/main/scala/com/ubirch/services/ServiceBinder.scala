package com.ubirch.services

import com.google.inject.AbstractModule
import com.typesafe.config.Config
import com.ubirch.services.cluster.{ ConnectionService, DefaultConnectionService }
import com.ubirch.services.config.ConfigProvider
import com.ubirch.services.lifeCycle.{ DefaultLifecycle, Lifecycle }

class ServiceBinder extends AbstractModule {

  override def configure() = {

    bind(classOf[Lifecycle]).to(classOf[DefaultLifecycle])
    bind(classOf[ClusterService]).to(classOf[DefaultClusterService])
    bind(classOf[ConnectionService]).to(classOf[DefaultConnectionService])
    bind(classOf[Config]).toProvider(classOf[ConfigProvider])

  }

}
