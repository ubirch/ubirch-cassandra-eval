package com.ubirch.models

import java.util.concurrent.atomic.AtomicInteger

import javax.inject._
import com.google.inject.{ AbstractModule, Guice }

trait Counter {
  def nextCount(): Int
}

@Singleton
class AtomicCounter extends Counter {
  private val atomicCounter = new AtomicInteger()
  override def nextCount(): Int = atomicCounter.getAndIncrement()
}

class Module extends AbstractModule {

  override def configure() = {
    // Set AtomicCounter as the implementation for Counter.
    bind(classOf[Counter]).to(classOf[AtomicCounter])
  }

}

object GuiceExample extends App {

  val injector = Guice.createInjector(new Module())

  val counter: Counter = injector.getInstance(classOf[Counter])

  val counter2: Counter = injector.getInstance(classOf[Counter])

  counter.nextCount()

  val countFromCounter = counter.nextCount()

  val countFromCounter2 = counter2.nextCount()

  assert(countFromCounter + 1 == countFromCounter2)

}
