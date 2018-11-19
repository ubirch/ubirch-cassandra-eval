package com.ubirch

import com.datastax.driver.core.exceptions.InvalidQueryException
import io.getquill.context.cassandra.CassandraContext
import io.getquill.context.cassandra.encoding.{ Decoders, Encoders }
import io.getquill.{ CassandraAsyncContext, SnakeCase }
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ BeforeAndAfterAll, BeforeAndAfterEach, MustMatchers, WordSpec }

import scala.concurrent.ExecutionContext.Implicits.global

trait TrafficLightSensorDAOBase {

  val db: CassandraContext[_] with Encoders with Decoders
  import db._

  case class TrafficLightSensor(country: String, city: String, sensorId: String, entry: Int, value: Int)

  val trafficLightSensors = List(
    TrafficLightSensor("DE", "Berlin", "1", 1, 1),
    TrafficLightSensor("CO", "Bogotá", "2", 2, 2),
    TrafficLightSensor("UK", "London", "3", 3, 3),
    TrafficLightSensor("USA", "NYC", "4", 4, 4))

  val insert = quote((e: TrafficLightSensor) => query[TrafficLightSensor].insert(e))
  val deleteAll = quote(query[TrafficLightSensor].delete)
  val selectAll = quote(query[TrafficLightSensor])
  val map = quote(query[TrafficLightSensor].map(_.city))
  val filter = quote(query[TrafficLightSensor].filter(_.city == "Berlin"))
  val filterAllowingFiltering = quote(query[TrafficLightSensor].filter(_.city == "Berlin").allowFiltering)
  val withFilter = quote(query[TrafficLightSensor].withFilter(_.country == "Berlin"))
  val take = quote(query[TrafficLightSensor].take(2))
  val count = quote(query[TrafficLightSensor].size)

}

class QuillOpsSpec extends WordSpec
  with ScalaFutures
  with BeforeAndAfterEach
  with BeforeAndAfterAll
  with MustMatchers
  with TrafficLightSensorDAOBase {

  val db = new CassandraAsyncContext(SnakeCase, "db")

  import db._

  override protected def beforeAll(): Unit = {
    await(db.run(deleteAll))
    await(db.run(liftQuery(trafficLightSensors).foreach(ws => insert(ws))))
  }

  override def afterAll(): Unit = {
    db.close()
  }

  "Quill Basic Ops Spec" must {

    "contain the same elems as inserted when doing a SELECT" in {

      await(db.run(selectAll)) must contain theSameElementsAs trafficLightSensors

    }

    "contain the same mapped elems as inserted when doing a MAP" in {

      await(db.run(map)) must contain theSameElementsAs trafficLightSensors.map(_.city)

    }

    "contain the same filtered elems as inserted when doing a FILTER ALLOWING FILTERING" in {

      await(db.run(filterAllowingFiltering)) must contain theSameElementsAs trafficLightSensors.filter(_.city == "Berlin")

    }

    "contain the same filtered elems as inserted when doing a FILTER" in {

      assertThrows[InvalidQueryException](await(db.run(filter)))

    }

    "contain the same values after TAKING 2 values" in {
      await(db.run(take)) must contain theSameElementsAs trafficLightSensors.take(2)
    }

    "have the same size" in {
      assert(await(db.run(count)) == trafficLightSensors.size)
    }

  }

}
