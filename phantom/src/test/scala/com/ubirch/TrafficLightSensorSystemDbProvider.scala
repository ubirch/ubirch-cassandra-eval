package com.ubirch

import com.outworkers.phantom.database.DatabaseProvider

trait TrafficLightSensorSystemDbProvider extends DatabaseProvider[TrafficLightSensorSystemDatabase] {
  override def database: TrafficLightSensorSystemDatabase = TrafficLightSensorSystemDatabase
}