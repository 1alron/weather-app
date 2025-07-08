package com.alron.weatherapp.api

data class ForecastWeatherResponse(
    val forecast: Forecast
)

data class Forecast(
    val forecastDays: List<ForecastDay>
)

data class ForecastDay(
    val date: String,
    val avgtemp_c: Double,
    val condition: WeatherCondition
)

