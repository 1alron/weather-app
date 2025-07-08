package com.alron.weatherapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alron.weatherapp.api.City
import com.alron.weatherapp.api.WeatherApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherAppViewModel @Inject constructor(
    private val weatherApiService: WeatherApiService
) : ViewModel() {
    private val _uiState = MutableStateFlow(WeatherAppUiState())
    val uiState: StateFlow<WeatherAppUiState> = _uiState

    fun onQueryChange(newQuery: String) {
        _uiState.update {
            it.copy(
                query = newQuery
            )
        }
        if (newQuery.length >= 3) {
            searchCities(newQuery)
        } else {
            _uiState.update {
                it.copy(
                    cityList = emptyList()
                )
            }
        }
    }

    fun onCitySelected(city: City) {
        _uiState.update {
            it.copy(
                currentCity = city,
                query = "",
                cityList = emptyList()
            )
        }
        loadWeather(city.name)
    }

    fun loadWeather(location: String) {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        currentWeather = weatherApiService.getCurrentWeather(location).current
                    )
                }
                val response = weatherApiService.getWeatherForecast(
                    location = location,
                    days = 5
                )
                _uiState.update {
                    it.copy(
                        forecast = response.forecast
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun searchCities(query: String) {
        viewModelScope.launch {
            try {
                val result = weatherApiService.searchCities(
                    query = query
                )
                _uiState.update {
                    it.copy(
                        cityList = result
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        cityList = emptyList()
                    )
                }
            }
        }
    }
}