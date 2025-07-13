package com.alron.weatherapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCache(cache: WeatherCache)

    @Query("SELECT * FROM weather_cache WHERE cityId = :cityId")
    suspend fun getCache(cityId: Int): WeatherCache?

    @Query("SELECT * FROM weather_cache WHERE isDefault = 1")
    suspend fun getDefaultLocation(): WeatherCache?

    @Query("UPDATE weather_cache SET isDefault = 0")
    suspend fun clearDefaultFlags()

    @Query("DELETE FROM weather_cache")
    suspend fun clearAll()
}