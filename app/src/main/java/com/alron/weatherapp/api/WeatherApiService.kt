package com.alron.weatherapp.api

import com.alron.weatherapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("search.json")
    suspend fun searchCities(
        @Query("key") key: String = BuildConfig.WEATHER_API_KEY,
        @Query("q") query: String
    ): List<City>

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("q") location: String,
        @Query("key") key: String = BuildConfig.WEATHER_API_KEY
    ): CurrentWeatherResponse

    @GET("forecast.json")
    suspend fun getWeatherForecast(
        @Query("q") location: String,
        @Query("days") days: Int = 5,
        @Query("key") key: String = BuildConfig.WEATHER_API_KEY
    ): ForecastWeatherResponse
}