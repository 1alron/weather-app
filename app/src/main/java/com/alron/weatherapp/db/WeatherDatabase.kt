package com.alron.weatherapp.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherCache::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}