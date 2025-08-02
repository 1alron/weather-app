package com.alron.weatherapp.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_cache")
data class WeatherCache(
    @PrimaryKey val cityId: Int,
    val cityName: String,
    val region: String,
    val country: String,
    val currentWeatherJson: String,
    val forecastJson: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isDefault: Boolean = false
)