package com.ubirch

import io.getquill._

import scala.concurrent.ExecutionContext.Implicits.global

object QuillSimpleQueries extends App {

  val db = new CassandraAsyncContext(SnakeCase, "db")

  import db._

  case class WeatherStation(country: String, city: String, stationId: String, entry: Int, value: Int)

  object WeatherStation {

    val getAllByCountry = quote {
      country: String => query[WeatherStation].filter(_.country == country)
    }
  }

  val result = db.run(WeatherStation.getAllByCountry(lift("UK")))

  result.onComplete(_ => db.close())
}
