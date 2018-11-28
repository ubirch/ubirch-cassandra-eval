package com.ubirch.cdi.services.execution

trait ExecutionProvider {
  implicit val ec = scala.concurrent.ExecutionContext.global
}
