package com.ubirch.macwire.models

import java.util.{ Date, UUID }

import com.ubirch.macwire.services.cluster.ConnectionServiceModule
import com.ubirch.macwire.services.execution.ExecutionProvider
import org.joda.time.DateTime

case class Event(
  id: UUID,
  principal: String,
  category: String,
  eventSourceService: String,
  deviceId: UUID,
  year: Int,
  month: Int,
  day: Int,
  hour: Int,
  minute: Int,
  second: Int,
  milli: Int,
  created: Date,
  updated: Date)

trait EventsQueries extends TablePointer[Event] {

  import db._

  implicit val eventSchemaMeta = schemaMeta[Event]("events")

  //These represent query descriptions only

  def selectAllQ = quote(query[Event])

  def byPrincipalAndCategoryQ(principal: String, category: String) = quote {
    query[Event]
      .filter(_.principal == lift(principal))
      .filter(_.category == lift(category))
  }

  def byPrincipalAndCatAndYearQ(principal: String, category: String, date: DateTime) = quote {
    query[Event]
      .filter(_.principal == lift(principal))
      .filter(_.category == lift(category))
      .filter(_.year == lift(date.year().get()))
  }

  def byPrincipalAndCatAndYearAndMonthQ(principal: String, category: String, date: DateTime) = quote {
    query[Event]
      .filter(_.principal == lift(principal))
      .filter(_.category == lift(category))
      .filter(_.year == lift(date.year().get()))
      .filter(_.month == lift(date.monthOfYear().get()))
  }

  def byPrincipalAndCatAndYearAndMonthAndDayQ(principal: String, category: String, date: DateTime) = quote {
    query[Event]
      .filter(_.principal == lift(principal))
      .filter(_.category == lift(category))
      .filter(_.year == lift(date.year().get()))
      .filter(_.month == lift(date.monthOfYear().get()))
      .filter(_.day == lift(date.dayOfMonth().get()))
  }

}

trait EventsBase extends EventsQueries with ConnectionServiceModule with ExecutionProvider {
  val db = context

  import db._

  //These actually run the queries.

  def selectAll = run(selectAllQ)

  def byPrincipalAndCategory(principal: String, category: String) =
    run(byPrincipalAndCategoryQ(principal, category))

  def byPrincipalAndCatAndYear(principal: String, category: String, date: DateTime) =
    run(byPrincipalAndCatAndYearQ(principal, category, date))

  def byPrincipalAndCatAndYearAndMonth(principal: String, category: String, date: DateTime) =
    run(byPrincipalAndCatAndYearAndMonthQ(principal, category, date))

  def byPrincipalAndCatAndYearAndMonthAndDay(principal: String, category: String, date: DateTime) =
    run(byPrincipalAndCatAndYearAndMonthAndDayQ(principal, category, date))

}

trait EventsModule {

  import com.softwaremill.macwire._

  protected class EventsProvider extends EventsBase

  def events = wire[EventsProvider]

}

trait EventsByCatQueries extends TablePointer[Event] {

  import db._

  implicit val eventSchemaMeta = schemaMeta[Event]("events_by_cat")

  //These represent query descriptions only

  def selectAllQ = quote(query[Event])

  def byCatAndEventSourceAndYearAndMonthQ(category: String, eventSourceService: String, date: DateTime) = quote {
    query[Event]
      .filter(_.category == lift(category))
      .filter(_.eventSourceService == lift(eventSourceService))
      .filter(_.year == lift(date.year().get()))
      .filter(_.month == lift(date.monthOfYear().get()))
  }

  def byCatAndEventSourceAndYearAndMonthAndDayQ(category: String, eventSourceService: String, date: DateTime) = quote {
    query[Event]
      .filter(_.category == lift(category))
      .filter(_.eventSourceService == lift(eventSourceService))
      .filter(_.year == lift(date.year().get()))
      .filter(_.month == lift(date.monthOfYear().get()))
      .filter(_.day == lift(date.dayOfMonth().get()))
  }

  def byCatAndEventSourceAndYearAndMonthAndDayAndDeviceIdQ(category: String, eventSourceService: String, date: DateTime, deviceId: UUID) = quote {
    query[Event]
      .filter(_.category == lift(category))
      .filter(_.eventSourceService == lift(eventSourceService))
      .filter(_.year == lift(date.year().get()))
      .filter(_.month == lift(date.monthOfYear().get()))
      .filter(_.day == lift(date.dayOfMonth().get()))
      .filter(_.hour == lift(date.hourOfDay().get()))
      .filter(_.deviceId == lift(deviceId))

  }

}

trait EventsByCatBase
  extends EventsByCatQueries
  with ConnectionServiceModule
  with ExecutionProvider {

  val db = context

  import db._

  //These actually run the queries.

  def selectAll = run(selectAllQ)

  def byCatAndEventSourceAndYearAndMonth(category: String, eventSourceService: String, date: DateTime) =
    run(byCatAndEventSourceAndYearAndMonthQ(category, eventSourceService, date))

  def byCatAndEventSourceAndYearAndMonthAndDay(category: String, eventSourceService: String, date: DateTime) =
    run(byCatAndEventSourceAndYearAndMonthAndDayQ(category, eventSourceService, date))

  def byCatAndEventSourceAndYearAndMonthAndDayAndDeviceId(category: String, eventSourceService: String, date: DateTime, deviceId: UUID) =
    run(byCatAndEventSourceAndYearAndMonthAndDayAndDeviceIdQ(category, eventSourceService, date, deviceId))

}

trait EventsByCatModule {
  import com.softwaremill.macwire._

  protected class EventsByCatProvider extends EventsByCatBase

  def eventsByCat = wire[EventsByCatProvider]

}