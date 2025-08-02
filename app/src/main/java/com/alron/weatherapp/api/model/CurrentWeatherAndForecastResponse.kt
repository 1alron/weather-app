package com.alron.weatherapp.api.model

data class CurrentWeatherAndForecastResponse(
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
    val maxwind_kph: Double,
    val avghumidity: Int,
    val condition: WeatherCondition
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