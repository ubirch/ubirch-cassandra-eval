package com.ubirch.guice

import com.google.inject.Guice
import com.typesafe.scalalogging.LazyLogging
import com.ubirch.guice.models.{ Events, EventsByCat }
import com.ubirch.guice.services.ServiceBinder
import com.ubirch.guice.services.lifeCycle.JVMHook

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

object run extends App with LazyLogging {

  def await[T](f: Future[T]): T = Await.result(f, Duration.Inf)

  val injector = Guice.createInjector(new ServiceBinder())
  injector.getInstance(classOf[JVMHook])

  val events = injector.getInstance(classOf[Events])

  val eventsByCat = injector.getInstance(classOf[EventsByCat])

  await(events.selectAll).foreach(x => println(x.deviceId))

  await(eventsByCat.selectAll).foreach(x => println(x.deviceId))

  logger.info("Press ^C to stop...")

}
