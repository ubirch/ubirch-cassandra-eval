package com.ubirch.macwire.services.lifeCycle

import java.util.concurrent.ConcurrentLinkedDeque

import com.typesafe.scalalogging.LazyLogging

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

private[lifeCycle] object LifecycleProvider extends LazyLogging {

  private val hooks = new ConcurrentLinkedDeque[() => Future[_]]()

  def addStopHook(hook: () => Future[_]): Unit = hooks.push(hook)

  def stop(): Future[_] = {

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

private[lifeCycle] class LifecycleProvider {

  def addStopHook(hook: () => Future[_]): Unit = LifecycleProvider.addStopHook(hook)

  def stop(): Future[_] = LifecycleProvider.stop()

}

trait LifecycleModule {
  import com.softwaremill.macwire._

  private def lifecycleProvider = wire[LifecycleProvider]

  object lifecycle {

    def addStopHook(hook: () => Future[_]): Unit = lifecycleProvider.addStopHook(hook)

    def stop(): Future[_] = lifecycleProvider.stop()

  }

}

trait JVMHook {
  protected def registerShutdownHooks(): Unit
}

trait JVMHookModule extends LifecycleModule with JVMHook with LazyLogging {

  protected def registerShutdownHooks() {

    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit = {
        lifecycle.stop()

        Thread.sleep(5000) //Waiting 5 secs
        logger.info("Bye bye, see you later...")
      }
    })

  }

  registerShutdownHooks()

}
