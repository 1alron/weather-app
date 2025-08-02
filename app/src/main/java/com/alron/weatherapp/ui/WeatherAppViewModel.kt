package com.alron.weatherapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alron.weatherapp.api.City
import com.alron.weatherapp.api.WeatherApiService
import com.alron.weatherapp.api.model.CurrentWeather
import com.alron.weatherapp.api.model.Forecast
import com.alron.weatherapp.api.model.ForecastDay
import com.alron.weatherapp.db.WeatherDao
import com.alron.weatherapp.db.model.WeatherCache
import com.alron.weatherapp.util.NUMBER_OF_DAYS_WITH_FORECAST
import com.alron.weatherapp.util.NUMBER_OF_SYMBOLS_SEARCH_START
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WeatherAppViewModel @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val weatherDao: WeatherDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(WeatherAppUiState())
    val uiState: StateFlow<WeatherAppUiState> = _uiState

    fun setDefaultLocation(
        city: City, weather: CurrentWeather,
        forecastday: List<ForecastDay>
    ) {
        viewModelScope.launch {
            weatherDao.clearDefaultFlags()
            val defaultCityCache = WeatherCache(
                cityId = city.id,
                cityName = city.name,
                region = city.region,
                country = city.country,
                currentWeatherJson = Gson().toJson(weather),
                forecastJson = Gson().toJson(Forecast(forecastday)),
                isDefault = true
            )
            weatherDao.insertCache(defaultCityCache)
        }
    }

    suspend fun getDefaultCity(): City? {
        return weatherDao.getDefaultLocation()?.let { cache ->
            City(
                id = cache.cityId,
                name = cache.cityName,
                region = cache.region,
                country = cache.country,
            )
        }
    }

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
                val forecastResponse = try {
                    weatherApiService
                        .getCurrentWeatherAndForecast(location, NUMBER_OF_DAYS_WITH_FORECAST)
                } catch (_: IOException) {
                    null
                }

                if (forecastResponse != null) {
                    val currentWeather = forecastResponse.current
                    val forecast = forecastResponse.forecast
                    val city = _uiState.value.currentCity!!
                    val existingCache = weatherDao.getCache(city.id)
                    val cache = WeatherCache(
                        cityId = city.id,
                        cityName = city.name,
                        region = city.region,
                        country = city.country,
                        currentWeatherJson = Gson().toJson(currentWeather),
                        forecastJson = Gson().toJson(forecast),
                        isDefault = existingCache?.isDefault ?: false
                    )
                    weatherDao.insertCache(cache)
                    _uiState.update {
                        it.copy(
                            currentWeather = currentWeather,
                            forecast = forecastResponse.forecast.forecastday,
                            isLoadingWeatherAndForecast = false,
                            weatherLoadError = null
                        )
                    }
                } else {
                    val cached = _uiState.value.currentCity?.let { city ->
                        weatherDao.getCache(city.id)
                    }
                    if (cached != null) {
                        val currentWeather = Gson().fromJson(
                            cached.currentWeatherJson,
                            CurrentWeather::class.java
                        )
                        val forecast = Gson().fromJson(
                            cached.forecastJson,
                            Forecast::class.java
                        )
                        _uiState.update {
                            it.copy(
                                currentWeather = currentWeather,
                                forecast = forecast.forecastday,
                                isLoadingWeatherAndForecast = false,
                                weatherLoadError = "Отсутствует подключение к интернету." +
                                        " Показаны последние сохраненные данные:"
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                currentWeather = null,
                                forecast = emptyList(),
                                isLoadingWeatherAndForecast = false,
                                weatherLoadError = "Отсутствует подключение к интернету."
                            )
                        }
                    }
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