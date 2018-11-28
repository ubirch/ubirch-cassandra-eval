package com.ubirch.cdi

import com.typesafe.scalalogging.LazyLogging
import com.ubirch.cdi.models.{ Events, EventsByCat, EventsByCatWithNamespace, EventsWithNamespace }
import com.ubirch.cdi.services.lifeCycle.DefaultJVMHook

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

object run
  extends App
  with DefaultJVMHook
  with LazyLogging {

  def await[T](f: Future[T]): T = Await.result(f, Duration.Inf)

  val runQueryEvents = await(Events.selectAll)
  runQueryEvents.foreach(x => println(x.deviceId))

  val runQueryEventsByCat = await(EventsByCat.selectAll)
  runQueryEventsByCat.foreach(x => println(x.deviceId))

  logger.info("Press ^C to stop...")

}

object run2
  extends App
  with DefaultJVMHook
  with EventsWithNamespace
  with EventsByCatWithNamespace
  with LazyLogging {

  def await[T](f: Future[T]): T = Await.result(f, Duration.Inf)

  val runQueryEvents = await(events.selectAll)
  runQueryEvents.foreach(x => println(x.deviceId))

  val runQueryEventsByCat = await(eventsByCat.selectAll)
  runQueryEventsByCat.foreach(x => println(x.deviceId))

  logger.info("Press ^C to stop...")

}

