package com.alron.weatherapp.ui

import com.alron.weatherapp.api.ForecastDay
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alron.weatherapp.api.City
import com.alron.weatherapp.api.CurrentWeather
import com.alron.weatherapp.api.CurrentWeatherResponse
import com.alron.weatherapp.api.ForecastWeatherResponse
import com.alron.weatherapp.api.WeatherApiService
import com.alron.weatherapp.db.WeatherCache
import com.alron.weatherapp.db.WeatherDao
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
        forecast: List<ForecastDay>
    ) {
        viewModelScope.launch {
            weatherDao.clearDefaultFlags()
            val defaultCityCache = WeatherCache(
                cityId = city.id,
                cityName = city.name,
                region = city.region,
                country = city.country,
                currentWeatherJson = Gson().toJson(weather),
                forecastJson = Gson().toJson(forecast),
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

                if (currentResponse != null && forecastResponse != null) {
                    val city = _uiState.value.currentCity!!
                    val existingCache = weatherDao.getCache(city.id)
                    val cache = WeatherCache(
                        cityId = city.id,
                        cityName = city.name,
                        region = city.region,
                        country = city.country,
                        currentWeatherJson = Gson().toJson(currentResponse),
                        forecastJson = Gson().toJson(forecastResponse),
                        isDefault = existingCache?.isDefault ?: false
                    )
                    weatherDao.insertCache(cache)
                    _uiState.update {
                        it.copy(
                            currentWeather = currentResponse.current,
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
                        val currentWeather = Gson().fromJson(cached.currentWeatherJson,
                            CurrentWeatherResponse::class.java)
                        val forecast = Gson().fromJson(cached.forecastJson,
                            ForecastWeatherResponse::class.java)
                        _uiState.update {
                            it.copy(
                                currentWeather = currentWeather.current,
                                forecast = forecast.forecast.forecastday,
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