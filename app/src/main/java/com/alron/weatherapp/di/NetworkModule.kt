package com.alron.weatherapp.di

import com.alron.weatherapp.BuildConfig
import com.alron.weatherapp.api.WeatherApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.WEATHER_API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideWeatherApi(retrofit: Retrofit): WeatherApiService =
        retrofit.create(WeatherApiService::class.java)
}