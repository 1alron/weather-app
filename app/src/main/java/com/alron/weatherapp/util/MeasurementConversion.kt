package com.alron.weatherapp.util

import kotlin.math.roundToInt

fun fromKilPerHourToMetPerSec(
    kilPerHour: Double
) = (kilPerHour / 3.6).roundToInt()