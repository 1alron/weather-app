package com.alron.weatherapp.api

data class CurrentWeatherResponse(
    val location: Location,
    val current: CurrentWeather
)

data class Location(
    val name: String,
    val country: String
)

data class CurrentWeather(
    val temp_c: Double,
    val condition: WeatherCondition,
    val wind_kph: Double,
    val humidity: Int
)

data class WeatherCondition(
    val text: String,
    val icon: String
)