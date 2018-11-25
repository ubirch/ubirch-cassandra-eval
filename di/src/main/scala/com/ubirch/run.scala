package com.ubirch

import com.google.inject.Guice
import com.ubirch.models.Events
import com.ubirch.services.ServiceBinder

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration.Duration

object run extends App {

  def await[T](f: Future[T]): T = Await.result(f, Duration.Inf)

  val injector = Guice.createInjector(new ServiceBinder())

  val events = injector.getInstance(classOf[Events])

  if (events != null) {
    val runQuery = await(events.selectAll)
    runQuery.foreach(x => println(x.deviceId))
  }

  events.db.close()

}
