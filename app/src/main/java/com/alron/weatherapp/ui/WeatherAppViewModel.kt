package com.alron.weatherapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alron.weatherapp.api.City
import com.alron.weatherapp.api.WeatherApiService
import com.alron.weatherapp.util.NUMBER_OF_DAYS_WITH_FORECAST
import com.alron.weatherapp.util.NUMBER_OF_SYMBOLS_SEARCH_START
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
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
        if (newQuery.length >= NUMBER_OF_SYMBOLS_SEARCH_START) {
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
        _uiState.update { it.copy(isLoadingWeatherAndForecast = true) }
        viewModelScope.launch {
            try {
                val currentResponse = try {
                    weatherApiService.getCurrentWeather(location)
                } catch (e: IOException) {
                    null
                }

                val forecastResponse = try {
                    weatherApiService.getWeatherForecast(location, NUMBER_OF_DAYS_WITH_FORECAST)
                } catch (e: IOException) {
                    null
                }

                _uiState.update {
                    it.copy(
                        currentWeather = currentResponse?.current,
                        forecast = forecastResponse?.forecast?.forecastday ?: emptyList(),
                        isLoadingWeatherAndForecast = false,
                        weatherLoadError = if (currentResponse == null || forecastResponse == null) {
                            "Нет подключения к интернету"
                        } else null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        currentWeather = null,
                        forecast = emptyList(),
                        isLoadingWeatherAndForecast = false,
                        weatherLoadError = "Ошибка загрузки: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    private fun searchCities(query: String) {
        viewModelScope.launch {
            try {
                val result =
                    weatherApiService.searchCities(
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