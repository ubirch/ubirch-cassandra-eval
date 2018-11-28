package com.ubirch.cdi

import com.typesafe.scalalogging.LazyLogging
import com.ubirch.cdi.models.{ Events, EventsByCat }
import com.ubirch.cdi.services.lifeCycle.DefaultLifecycle

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }
import scala.io.StdIn

object run extends App with DefaultLifecycle with LazyLogging {

  def await[T](f: Future[T]): T = Await.result(f, Duration.Inf)

  val runQueryEvents = await(Events.selectAll)
  runQueryEvents.foreach(x => println(x.deviceId))

  val runQueryEventsByCat = await(EventsByCat.selectAll)
  runQueryEventsByCat.foreach(x => println(x.deviceId))

  logger.info("Press RETURN to stop...")
  StdIn.readLine() // let it run until user presses return

  stop()

}
