package com.alron.weatherapp.ui

import com.alron.weatherapp.api.ForecastDay
import com.alron.weatherapp.api.City
import com.alron.weatherapp.api.CurrentWeather

data class WeatherAppUiState(
    val query: String = "",
    val cityList: List<City> = emptyList(),
    val currentCity: City? = null,
    val currentWeather: CurrentWeather? = null,
    val forecast: List<ForecastDay> = emptyList(),
    val isLoadingWeatherAndForecast: Boolean = false,
    val weatherLoadError: String? = null
)