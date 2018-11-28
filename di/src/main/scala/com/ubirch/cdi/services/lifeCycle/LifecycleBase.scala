package com.ubirch.cdi.services.lifeCycle

import java.util.concurrent.ConcurrentLinkedDeque

import com.typesafe.scalalogging.LazyLogging

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

private[lifeCycle] object LifecycleProvider
  extends LifecycleBase
  with LazyLogging {

  private val hooks = new ConcurrentLinkedDeque[() => Future[_]]()

  override def addStopHook(hook: () => Future[_]): Unit = hooks.push(hook)

  override def stop(): Future[_] = {

    @tailrec
    def clearHooks(previous: Future[Any] = Future.successful[Any](())): Future[Any] = {
      val hook = hooks.poll()
      if (hook != null) clearHooks(previous.flatMap { _ =>
        hook().recover {
          case e => logger.error("Error executing stop hook", e)
        }
      })
      else previous
    }

    logger.info("Running life cycle hooks...")
    clearHooks()
  }
}

trait LifecycleBase {

  def addStopHook(hook: () => Future[_]): Unit

  def stop(): Future[_]

}

trait DefaultLifecycle extends LifecycleBase {

  def addStopHook(hook: () => Future[_]): Unit = LifecycleProvider.addStopHook(hook)

  def stop(): Future[_] = LifecycleProvider.stop()
}
