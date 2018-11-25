package com.ubirch.services

import com.google.inject.AbstractModule
import com.ubirch.services.cluster.{ ConnectionService, DefaultConnectionService }

class ServiceBinder extends AbstractModule {

  override def configure() = {

    bind(classOf[ClusterService]).to(classOf[DefaultClusterService])
    bind(classOf[ConnectionService]).to(classOf[DefaultConnectionService])

  }

}
