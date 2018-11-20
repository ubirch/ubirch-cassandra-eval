package com.ubirch

import org.scalatest.{ BeforeAndAfterAll, BeforeAndAfterEach, MustMatchers, WordSpec }
import org.scalatest.concurrent.ScalaFutures
import com.outworkers.phantom.dsl._

class PhantomSpec extends WordSpec
  with ScalaFutures
  with BeforeAndAfterEach
  with BeforeAndAfterAll
  with MustMatchers
  with TrafficLightSensorSystemDbProvider {

  override def beforeAll(): Unit = {
    super.beforeAll()
    database.create()
  }

  override protected def afterAll(): Unit = {
    super.beforeAll()
    database.shutdown()
  }

  "PhantomSpec" must {

    "do simple query getting all the object" in {

      val sample = TrafficLightSensor("DE", "Berlin", "1", 1, 1)

      val chain = for {
        _ <- database.TrafficLightSensor.store(sample).future()
        res <- database.TrafficLightSensor.findTrafficLightSensor(sample.country)
      } yield res

      whenReady(chain) { res =>
        res mustBe defined
        res mustEqual Some(sample)
      }

    }

    "do simple query getting one column" in {

      val sample = TrafficLightSensor("DE", "Berlin", "1", 1, 1)

      val chain = for {
        _ <- database.TrafficLightSensor.store(sample).future()
        res <- database.TrafficLightSensor.findTrafficLightSensorCountry(sample.country)
      } yield res

      whenReady(chain) { res =>
        res mustBe defined
        res mustEqual Some(sample.country)
      }

    }

    "do update" in {

      val sample = TrafficLightSensor("DE", "Berlin", "1", 1, 1)
      val newEntryId = 2

      val chain = for {
        _ <- database.TrafficLightSensor.store(sample).future()
        res <- database.TrafficLightSensor.findTrafficLightSensor(sample.country)
        _ <- database.TrafficLightSensor.updateEntry(sample.country, sample.city, sample.sensorId, newEntryId)
        res2 <- database.TrafficLightSensor.findTrafficLightSensor(sample.country)
      } yield (res, res2)

      whenReady(chain) {
        case (res, res2) =>
          res mustBe defined
          res mustEqual Some(sample)
          res2 mustBe defined
          res2 mustEqual Some(sample.copy(entry = newEntryId))
      }

    }

  }

}
