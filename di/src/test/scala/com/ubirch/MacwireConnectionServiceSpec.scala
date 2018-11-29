package com.ubirch

import java.util.{ Date, UUID }

import com.ubirch.macwire.models.Event
import com.ubirch.macwire.models.{ EventsByCatModule, EventsModule }
import com.ubirch.macwire.services.cluster.ConnectionServiceModule
import org.joda.time.DateTime
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ BeforeAndAfterAll, BeforeAndAfterEach, MustMatchers, WordSpec }

class MacwireConnectionServiceSpec extends WordSpec
  with ScalaFutures
  with BeforeAndAfterEach
  with BeforeAndAfterAll
  with MustMatchers
  // Adding Tables //
  with EventsModule
  with EventsByCatModule {

  val eventsToExpect = List(
    Event(UUID.fromString("b5be9010-ed0e-11e8-82a0-53d40fda30be"), "RMunichRET", "Anchor", "Avatar-Service", UUID.fromString("41245902-69a0-450c-8d37-78e34f0e6760"), 2018, 11, 2, 11, 17, 0, 0, new Date(), new Date()),
    Event(UUID.fromString("b5bb34b0-ed0e-11e8-82a0-53d40fda30be"), "Regio IT", "Anchor", "Avatar-Service", UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"), 2018, 11, 2, 8, 17, 0, 0, new Date(), new Date()),
    Event(UUID.fromString("b5b456e0-ed0e-11e8-82a0-53d40fda30be"), "Regio IT", "Anchor", "Avatar-Service", UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"), 2018, 11, 1, 7, 17, 0, 0, new Date(), new Date()),
    Event(UUID.fromString("b5bda5b0-ed0e-11e8-82a0-53d40fda30be"), "MunichRE", "Anchor", "Avatar-Service", UUID.fromString("41245902-69a0-450c-8d37-78e34f0e6760"), 2018, 11, 1, 9, 17, 0, 0, new Date(), new Date()),
    Event(UUID.fromString("b5b51a30-ed0e-11e8-82a0-53d40fda30be"), "Regio IT", "Validate", "Avatar-Service", UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"), 2018, 11, 2, 8, 15, 0, 0, new Date(), new Date()),
    Event(UUID.fromString("b5a38e00-ed0e-11e8-82a0-53d40fda30be"), "Regio IT", "Validate", "Avatar-Service", UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"), 2018, 11, 1, 7, 15, 0, 0, new Date(), new Date()),
    Event(UUID.fromString("b5be1ae0-ed0e-11e8-82a0-53d40fda30be"), "MunichRE", "Validate", "Avatar-Service", UUID.fromString("41245902-69a0-450c-8d37-78e34f0e6760"), 2018, 11, 2, 11, 15, 0, 0, new Date(), new Date()),
    Event(UUID.fromString("b5bbd0f0-ed0e-11e8-82a0-53d40fda30be"), "MunichRE", "Validate", "Avatar-Service", UUID.fromString("41245902-69a0-450c-8d37-78e34f0e6760"), 2018, 10, 1, 9, 15, 0, 0, new Date(), new Date()))

  val eventsByCatToExpect = List(
    Event(UUID.fromString("b5be9010-ed0e-11e8-82a0-53d40fda30be"), "RMunichRET", "Anchor", "Avatar-Service", UUID.fromString("41245902-69a0-450c-8d37-78e34f0e6760"), 2018, 11, 2, 11, 17, 0, 0, new Date(), new Date()),
    Event(UUID.fromString("b5bb34b0-ed0e-11e8-82a0-53d40fda30be"), "Regio IT", "Anchor", "Avatar-Service", UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"), 2018, 11, 2, 8, 17, 0, 0, new Date(), new Date()),
    Event(UUID.fromString("b5b456e0-ed0e-11e8-82a0-53d40fda30be"), "Regio IT", "Anchor", "Avatar-Service", UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"), 2018, 11, 1, 7, 17, 0, 0, new Date(), new Date()),
    Event(UUID.fromString("b5bda5b0-ed0e-11e8-82a0-53d40fda30be"), "MunichRE", "Anchor", "Avatar-Service", UUID.fromString("41245902-69a0-450c-8d37-78e34f0e6760"), 2018, 11, 1, 9, 17, 0, 0, new Date(), new Date()),
    Event(UUID.fromString("b5b51a30-ed0e-11e8-82a0-53d40fda30be"), "Regio IT", "Validate", "Avatar-Service", UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"), 2018, 11, 2, 8, 15, 0, 0, new Date(), new Date()),
    Event(UUID.fromString("b5a38e00-ed0e-11e8-82a0-53d40fda30be"), "Regio IT", "Validate", "Avatar-Service", UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"), 2018, 11, 1, 7, 15, 0, 0, new Date(), new Date()),
    Event(UUID.fromString("b5be1ae0-ed0e-11e8-82a0-53d40fda30be"), "MunichRE", "Validate", "Avatar-Service", UUID.fromString("41245902-69a0-450c-8d37-78e34f0e6760"), 2018, 11, 2, 11, 15, 0, 0, new Date(), new Date()),
    Event(UUID.fromString("b5bbd0f0-ed0e-11e8-82a0-53d40fda30be"), "MunichRE", "Validate", "Avatar-Service", UUID.fromString("41245902-69a0-450c-8d37-78e34f0e6760"), 2018, 10, 1, 9, 15, 0, 0, new Date(), new Date()))

  override def afterAll(): Unit = {

    val connectionModule = new ConnectionServiceModule {}

    val db = connectionModule.context

    db.close()

  }

  "Events" must {

    "select * from events;" in {

      val runQuery = await(events.selectAll)

      runQuery.map(_.deviceId) must contain theSameElementsInOrderAs eventsToExpect.map(_.deviceId)

    }

    "select * from events where principal = 'Regio IT' and category = 'Anchor';" in {

      val runQuery = await {
        events.byPrincipalAndCategory(
          "Regio IT",
          "Anchor")
      }

      val expected = List(
        UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"),
        UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"))

      runQuery.map(_.deviceId) must contain theSameElementsInOrderAs expected

    }

    "select * from events where principal = 'Regio IT' and category = 'Anchor' and year = 2018;" in {

      val date = new DateTime().withYear(2018)

      val runQuery = await {
        events.byPrincipalAndCatAndYear(
          "Regio IT",
          "Anchor",
          date)
      }

      val expected = List(
        UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"),
        UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"))

      runQuery.map(_.deviceId) must contain theSameElementsInOrderAs expected

    }

    "select * from events where principal = 'Regio IT' and category = 'Anchor' and year = 2018 and month = 11;" in {

      val date = new DateTime()
        .withYear(2018)
        .withMonthOfYear(11)

      val runQuery = await {
        events.byPrincipalAndCatAndYearAndMonth(
          "Regio IT",
          "Anchor",
          date)
      }

      val expected = List(
        UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"),
        UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"))

      runQuery.map(_.deviceId) must contain theSameElementsInOrderAs expected

    }

    "select * from events where principal = 'Regio IT' and category = 'Anchor' and year = 2018 and month = 11 and day = 1;" in {

      val date = new DateTime()
        .withYear(2018)
        .withMonthOfYear(11)
        .withDayOfMonth(1)

      val runQuery = await {
        events.byPrincipalAndCatAndYearAndMonthAndDay(
          "Regio IT",
          "Anchor",
          date)
      }

      val expected = List(
        UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"))

      runQuery.map(_.deviceId) must contain theSameElementsInOrderAs expected

    }

  }

  "EventsByCat" must {

    "select * from events_by_cat;" in {

      val runQuery = await(eventsByCat.selectAll)

      val expected = List(
        UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"),
        UUID.fromString("41245902-69a0-450c-8d37-78e34f0e6760"),
        UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"),
        UUID.fromString("41245902-69a0-450c-8d37-78e34f0e6760"),
        UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"),
        UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"),
        UUID.fromString("41245902-69a0-450c-8d37-78e34f0e6760"),
        UUID.fromString("41245902-69a0-450c-8d37-78e34f0e6760"))

      runQuery.map(_.deviceId) must contain theSameElementsInOrderAs expected

    }

    "select * from events_by_cat where category = 'Anchor' and event_source_service = 'Avatar-Service' and year = 2018 and month = 11;" in {

      val date = new DateTime()
        .withYear(2018)
        .withMonthOfYear(11)

      val runQuery = await {
        eventsByCat.byCatAndEventSourceAndYearAndMonth(
          "Anchor",
          "Avatar-Service",
          date)
      }

      val expected = List(
        UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"),
        UUID.fromString("41245902-69a0-450c-8d37-78e34f0e6760"),
        UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"),
        UUID.fromString("41245902-69a0-450c-8d37-78e34f0e6760"))

      runQuery.map(_.deviceId) must contain theSameElementsInOrderAs expected

    }

    "select * from events_by_cat where category = 'Anchor' and event_source_service = 'Avatar-Service' and year = 2018 and month = 11 and day = 2;" in {

      val date = new DateTime()
        .withYear(2018)
        .withMonthOfYear(11)
        .withDayOfMonth(2)

      val runQuery = await {
        eventsByCat.byCatAndEventSourceAndYearAndMonthAndDay(
          "Anchor",
          "Avatar-Service",
          date)
      }

      val expected = List(
        UUID.fromString("522f3e64-6ee5-470c-8b66-9edb0cfbf3b1"),
        UUID.fromString("41245902-69a0-450c-8d37-78e34f0e6760"))

      runQuery.map(_.deviceId) must contain theSameElementsInOrderAs expected

    }

    "select * from events_by_cat where category = 'Anchor' and event_source_service = 'Avatar-Service' and year = 2018 and month = 11 and day = 2 and hour = 11 and device_id = 41245902-69a0-450c-8d37-78e34f0e6760;" in {

      val date = new DateTime()
        .withYear(2018)
        .withMonthOfYear(11)
        .withDayOfMonth(2)
        .withHourOfDay(11)

      val runQuery = await {
        eventsByCat.byCatAndEventSourceAndYearAndMonthAndDayAndDeviceId(
          "Anchor",
          "Avatar-Service",
          date,
          UUID.fromString("41245902-69a0-450c-8d37-78e34f0e6760"))
      }

      val expected = List(UUID.fromString("41245902-69a0-450c-8d37-78e34f0e6760"))

      runQuery.map(_.deviceId) must contain theSameElementsInOrderAs expected

    }

  }

}
