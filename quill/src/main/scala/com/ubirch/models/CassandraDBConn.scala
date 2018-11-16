package com.ubirch.models

import io.getquill.{CassandraAsyncContext, SnakeCase}

trait CassandraDBConn {

  val db = new CassandraAsyncContext(SnakeCase, "db")
  
}
