package com.alron.weatherapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alron.weatherapp.BuildConfig
import com.alron.weatherapp.api.ApiClient
import com.alron.weatherapp.api.City
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherAppViewModel : ViewModel() {
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
            )
        }
    }

    private fun searchCities(query: String) {
        viewModelScope.launch {
            try {
                val result = ApiClient.weatherApi.searchCities(
                    key = BuildConfig.WEATHER_API_KEY,
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