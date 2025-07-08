package com.alron.weatherapp.ui

import com.alron.weatherapp.api.City
import com.alron.weatherapp.api.CurrentWeather
import com.alron.weatherapp.api.Forecast

data class WeatherAppUiState(
    val query: String = "",
    val cityList: List<City> = emptyList(),
    val currentCity: City? = null,
    val currentWeather: CurrentWeather? = null,
    val forecast: Forecast? = null
)