package com.ubirch

import com.google.inject.Guice
import com.typesafe.scalalogging.LazyLogging
import com.ubirch.models.Events
import com.ubirch.services.ServiceBinder
import com.ubirch.services.lifeCycle.Lifecycle

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration.Duration
import scala.io.StdIn

object run extends App with LazyLogging {

  def await[T](f: Future[T]): T = Await.result(f, Duration.Inf)

  val injector = Guice.createInjector(new ServiceBinder())
  val events = injector.getInstance(classOf[Events])
  val lifeCycle = injector.getInstance(classOf[Lifecycle])

  if (events != null) {
    val runQuery = await(events.selectAll)
    runQuery.foreach(x => println(x.deviceId))
  }

  logger.info("Press RETURN to stop...")
  StdIn.readLine() // let it run until user presses return

  lifeCycle.stop()

}
