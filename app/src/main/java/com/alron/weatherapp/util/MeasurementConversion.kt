package com.alron.weatherapp.util

import kotlin.math.roundToInt

fun fromKilPerHourToMetPerSec(
    kilPerHour: Double
) = (kilPerHour / 3.6).roundToInt()

fun fromMaxAndMinTempToAvg(
    maxTemp: Double,
    minTemp: Double
) = listOf(maxTemp, minTemp).average().roundToInt()

// not using DateTimeFormatter or something like that because we have to support Android API's < 26
fun formatDateToRussian(inputDate: String): String {
    val parts = inputDate.split("-")
    val day = parts[2].toInt()
    val month = parts[1].toInt()

    val monthNames = listOf(
        "января", "февраля", "марта", "апреля", "мая", "июня",
        "июля", "августа", "сентября", "октября", "ноября", "декабря"
    )

    return "$day ${monthNames[month - 1]}"
}