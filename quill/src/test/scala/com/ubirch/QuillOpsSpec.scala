package com.ubirch

import com.datastax.driver.core.exceptions.InvalidQueryException
import io.getquill.context.cassandra.CassandraContext
import io.getquill.context.cassandra.encoding.{ Decoders, Encoders }
import io.getquill.{ CassandraAsyncContext, SnakeCase }
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ BeforeAndAfterAll, BeforeAndAfterEach, MustMatchers, WordSpec }

import scala.concurrent.ExecutionContext.Implicits.global

trait WeatherStationDAOBase {

  val db: CassandraContext[_] with Encoders with Decoders
  import db._

  case class WeatherStation(country: String, city: String, stationId: String, entry: Int, value: Int)

  val weatherStations = List(
    WeatherStation("DE", "Berlin", "1", 1, 1),
    WeatherStation("CO", "Bogotá", "2", 2, 2),
    WeatherStation("UK", "London", "3", 3, 3),
    WeatherStation("USA", "NYC", "4", 4, 4))

  val insert = quote((e: WeatherStation) => query[WeatherStation].insert(e))
  val deleteAll = quote(query[WeatherStation].delete)
  val selectAll = quote(query[WeatherStation])
  val map = quote(query[WeatherStation].map(_.city))
  val filter = quote(query[WeatherStation].filter(_.city == "Berlin"))
  val filterAllowingFiltering = quote(query[WeatherStation].filter(_.city == "Berlin").allowFiltering)
  val withFilter = quote(query[WeatherStation].withFilter(_.country == "Berlin"))
  val take = quote(query[WeatherStation].take(2))
  val entitySize = quote(query[WeatherStation].size)

}

class QuillOpsSpec extends WordSpec
  with ScalaFutures
  with BeforeAndAfterEach
  with BeforeAndAfterAll
  with MustMatchers
  with WeatherStationDAOBase {

  val db = new CassandraAsyncContext(SnakeCase, "db")

  import db._

  override protected def beforeAll(): Unit = {
    await(db.run(deleteAll))
    await(db.run(liftQuery(weatherStations).foreach(ws => insert(ws))))
  }

  override def afterAll(): Unit = {
    db.close()
  }

  "Quill Basic Ops Spec" must {

    "must contain the same elems as inserted when doing a SELECT" in {

      await(db.run(selectAll)) must contain theSameElementsAs weatherStations

    }

    "must contain the same mapped elems as inserted when doing a MAP" in {

      await(db.run(map)) must contain theSameElementsAs weatherStations.map(_.city)

    }

    "must contain the same filtered elems as inserted when doing a FILTER ALLOWING FILTERING" in {

      await(db.run(filterAllowingFiltering)) must contain theSameElementsAs weatherStations.filter(_.city == "Berlin")

    }

    "must contain the same filtered elems as inserted when doing a FILTER" in {

      assertThrows[InvalidQueryException](await(db.run(filter)))

    }

  }

}
