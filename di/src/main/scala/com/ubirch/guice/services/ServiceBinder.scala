package com.ubirch.guice.services

import com.google.inject.AbstractModule
import com.typesafe.config.Config
import com.ubirch.guice.services.cluster.{ ConnectionService, DefaultConnectionService }
import com.ubirch.guice.services.config.ConfigProvider
import com.ubirch.guice.services.execution.ExecutionProvider
import com.ubirch.guice.services.lifeCycle.{ DefaultJVMHook, DefaultLifecycle, JVMHook, Lifecycle }

import scala.concurrent.ExecutionContext

class ServiceBinder extends AbstractModule {

  override def configure() = {

    bind(classOf[Lifecycle]).to(classOf[DefaultLifecycle])
    bind(classOf[ClusterService]).to(classOf[DefaultClusterService])
    bind(classOf[ConnectionService]).to(classOf[DefaultConnectionService])
    bind(classOf[JVMHook]).to(classOf[DefaultJVMHook])

    bind(classOf[Config]).toProvider(classOf[ConfigProvider])
    bind(classOf[ExecutionContext]).toProvider(classOf[ExecutionProvider])

  }

}