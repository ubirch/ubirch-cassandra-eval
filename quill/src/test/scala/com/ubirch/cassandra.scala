package com

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.Duration

import io.getquill._
import io.getquill.Literal
import io.getquill.CassandraStreamContext

package object ubirch {

  val r = scala.util.Random

  def await[T](f: Future[T]): T = Await.result(f, Duration.Inf)
}
