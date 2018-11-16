package com.ubirch

import io.getquill.{CassandraAsyncContext, SnakeCase}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, MustMatchers, WordSpec}
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext.Implicits.global

import io.getquill._

class QuillSpec   extends WordSpec
  with ScalaFutures
  with BeforeAndAfterEach
  with BeforeAndAfterAll
  with MustMatchers {

  "QuillSpec"  must {

    "do simple query" in {

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

  }


}
