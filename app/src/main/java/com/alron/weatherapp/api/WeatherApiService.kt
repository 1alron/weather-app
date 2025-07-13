package com.alron.weatherapp.api

import com.alron.weatherapp.BuildConfig
import com.alron.weatherapp.util.NUMBER_OF_DAYS_WITH_FORECAST
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("search.json")
    suspend fun searchCities(
        @Query("q") query: String,
        @Query("lang") language: String = "ru",
        @Query("key") key: String = BuildConfig.WEATHER_API_KEY,
    ): List<City>

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("q") location: String,
        @Query("lang") language: String = "ru",
        @Query("key") key: String = BuildConfig.WEATHER_API_KEY
    ): CurrentWeatherResponse

    @GET("forecast.json")
    suspend fun getWeatherForecast(
        @Query("q") location: String,
        @Query("days") days: Int = NUMBER_OF_DAYS_WITH_FORECAST,
        @Query("lang") language: String = "ru",
        @Query("key") key: String = BuildConfig.WEATHER_API_KEY
    ): ForecastWeatherResponse
}