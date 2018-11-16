package com.ubirch.models

import scala.concurrent.ExecutionContext.Implicits.global

case class WeatherStation(country: String, city: String, stationId: String, entry: Int, value: Int)

object WeatherStation extends CassandraDBConn {

  import db._

  val getAllByCountryQuote = quote {
    country: String => query[WeatherStation].filter(_.country == country)
  }

  val getAllByCountryAndCityQuote = quote {
    (country: String, city: String) =>
      getAllByCountryQuote(country).filter(_.city == city)
  }

  val getAllByCountryCityAndIdQuote = quote {
    (country: String, city: String, stationId: String) =>
      getAllByCountryAndCityQuote(country, city).filter(_.stationId == stationId)
  }


  def getAllByCountry(country: String) = db.run(getAllByCountryQuote(lift(country)))



}