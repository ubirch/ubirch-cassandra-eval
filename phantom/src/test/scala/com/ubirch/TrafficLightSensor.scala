package com.ubirch

import com.outworkers.phantom.dsl._

import scala.concurrent.{ Future => ScalaFuture }

case class TrafficLightSensor(
  country: String,
  city: String,
  sensorId: String,
  entry: Int,
  value: Int)

abstract class TrafficLightSensors extends Table[TrafficLightSensors, TrafficLightSensor] {

  override lazy val tableName = "traffic_light_sensors_ph"

  object country extends StringColumn with PartitionKey

  object city extends StringColumn with PrimaryKey

  object sensorId extends StringColumn with PrimaryKey

  object entry extends IntColumn

  object value extends IntColumn

  def findTrafficLightSensor(country: String): ScalaFuture[Option[TrafficLightSensor]] = {
    select.where(_.country eqs country).one()
  }

  def findTrafficLightSensorCountry(country: String): ScalaFuture[Option[String]] = {
    select(_.country).where(_.country eqs country).one()
  }

  def updateEntry(country: String, city: String, sensorId: String, newEntry: Int): ScalaFuture[ResultSet] = {
    update
      .where(_.country eqs country)
      .and(_.city eqs city)
      .and(_.sensorId eqs sensorId)
      .modify(_.entry setTo newEntry).future()
  }

  def deleteCountry(country: String): ScalaFuture[ResultSet] = {
    delete.where(_.country eqs country).future()
  }

}