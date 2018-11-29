package com.ubirch.macwire

import com.typesafe.scalalogging.LazyLogging
import com.ubirch.macwire.models.{ EventsByCatModule, EventsModule }
import com.ubirch.macwire.services.lifeCycle.JVMHookModule

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

object ImportedModule
  extends App
  with LazyLogging {

  def await[T](f: Future[T]): T = Await.result(f, Duration.Inf)

  val EventsSystem = new JVMHookModule with EventsModule with EventsByCatModule

  import EventsSystem._

  val runQueryEvents = await(events.selectAll)
  runQueryEvents.foreach(x => println(x.deviceId))

  val runQueryEventsByCat = await(eventsByCat.selectAll)
  runQueryEventsByCat.foreach(x => println(x.deviceId))

  logger.info("Press ^C to stop...")

}
