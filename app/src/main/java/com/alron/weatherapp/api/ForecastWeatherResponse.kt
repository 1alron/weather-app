package com.alron.weatherapp.api

data class ForecastWeatherResponse(
    val location: Location,
    val current: CurrentWeather,
    val forecast: Forecast
)

data class Forecast(
    val forecastday: List<ForecastDay>
)

data class ForecastDay(
    val date: String,
    val day: DayForecast
)

data class DayForecast(
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val condition: WeatherCondition
)
