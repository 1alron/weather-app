package com.alron.weatherapp.api

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("search.json")
    suspend fun searchCities(
        @Query("key") key: String,
        @Query("q") query: String
    ): List<City>
}