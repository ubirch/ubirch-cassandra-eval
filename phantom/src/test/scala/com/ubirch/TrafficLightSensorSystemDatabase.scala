package com.ubirch

import com.outworkers.phantom.connectors
import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.database.Database

class TrafficLightSensorSystemDatabase(override val connector: CassandraConnection)
  extends Database[TrafficLightSensorSystemDatabase](connector) {

  object TrafficLightSensor extends TrafficLightSensors with Connector

}

object TrafficLightSensorSystemDatabase extends TrafficLightSensorSystemDatabase(connectors.ContactPoint.local.keySpace("db"))

