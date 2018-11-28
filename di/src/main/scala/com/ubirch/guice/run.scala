package com.ubirch.guice

import com.google.inject.{ Guice, Injector }
import com.typesafe.scalalogging.LazyLogging
import com.ubirch.guice.models.{ Events, EventsByCat }
import com.ubirch.guice.services.ServiceBinder
import com.ubirch.guice.services.lifeCycle.JVMHook

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }
import scala.reflect._

trait Boot {

  val injector: Injector = Guice.createInjector(new ServiceBinder())

  private def bootJVMHook() = get[JVMHook]

  def get[T](clazz: Class[T]): T = injector.getInstance(clazz)

  def get[T](implicit ct: ClassTag[T]): T = get(ct.runtimeClass.asInstanceOf[Class[T]])

  def getAsOption[T](implicit ct: ClassTag[T]): Option[T] = Option(get(ct))

  bootJVMHook()

}

object run extends App with Boot with LazyLogging {

  def await[T](f: Future[T]): T = Await.result(f, Duration.Inf)

  val events = get[Events]

  val eventsByCat = get[EventsByCat]

  await(events.selectAll).foreach(x => println(x.deviceId))

  await(eventsByCat.selectAll).foreach(x => println(x.deviceId))

  logger.info("Press ^C to stop...")

}
