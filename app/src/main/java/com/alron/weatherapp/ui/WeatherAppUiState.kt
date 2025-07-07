package com.alron.weatherapp.ui

import com.alron.weatherapp.api.City

data class WeatherAppUiState(
    val query: String = "",
    val cityList: List<City> = emptyList(),
    val currentCity: City? = null,
)