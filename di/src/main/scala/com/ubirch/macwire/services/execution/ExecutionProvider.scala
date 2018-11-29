package com.ubirch.macwire.services.execution

trait ExecutionProvider {
  implicit val ec = scala.concurrent.ExecutionContext.global
}
