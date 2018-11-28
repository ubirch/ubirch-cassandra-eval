package com.ubirch.guice.services.execution

import javax.inject._

import scala.concurrent.ExecutionContext

class ExecutionProvider extends Provider[ExecutionContext] {
  override def get(): ExecutionContext = scala.concurrent.ExecutionContext.global
}
