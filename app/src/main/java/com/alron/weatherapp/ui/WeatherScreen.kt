package com.alron.weatherapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.alron.weatherapp.R
import com.alron.weatherapp.api.ForecastDay
import com.alron.weatherapp.util.NUMBER_OF_DAYS_WITH_FORECAST
import com.alron.weatherapp.util.formatDateToRussian
import com.alron.weatherapp.util.fromKilPerHourToMetPerSec
import com.alron.weatherapp.util.fromMaxAndMinTempToAvg
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlin.math.roundToInt

@Composable
fun WeatherScreen(
    weatherAppUiState: WeatherAppUiState,
    onSearchButtonClicked: () -> Unit,
    onSetDefaultLocation: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val swipeState =
        rememberSwipeRefreshState(
            isRefreshing = weatherAppUiState.isLoadingWeatherAndForecast
        )
    SwipeRefresh(
        state = swipeState,
        onRefresh = { onRefresh() }
    ) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        top = dimensionResource(R.dimen.padding_top_bar),
                        end = dimensionResource(R.dimen.padding_medium)
                    )
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                ) {
                    WeatherScreenTopBar(
                        onClick = onSearchButtonClicked,
                        modifier = Modifier
                    )
                }
                if (weatherAppUiState.currentCity == null) {
                    Spacer(Modifier.height(dimensionResource(R.dimen.weather_screen_spacer_height)))
                    Text(
                        text = stringResource(R.string.start_text),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                } else if (weatherAppUiState.currentWeather != null &&
                    weatherAppUiState.forecast.isNotEmpty()
                ) {
                    Column {
                        Spacer(Modifier.height(dimensionResource(R.dimen.weather_screen_spacer_height)))
                        CurrentWeather(
                            weatherAppUiState = weatherAppUiState
                        )
                        Spacer(Modifier.height(dimensionResource(R.dimen.weather_screen_spacer_height)))
                        Text(
                            text = stringResource(
                                R.string.weather_forecast_some_days,
                                NUMBER_OF_DAYS_WITH_FORECAST
                            ),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(
                                dimensionResource(R.dimen.padding_small)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            itemsIndexed(weatherAppUiState.forecast) { _, forecastDay ->
                                ForecastDayItem(forecastDay)
                            }
                        }
                        Spacer(
                            Modifier.height(
                                dimensionResource(
                                    R.dimen.weather_screen_spacer_height
                                )
                            )
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            WeatherAppButton(
                                onClick = onSetDefaultLocation,
                                imageVector = Icons.Default.LocationOn,
                                contentDescriptionId = R.string.set_default_location,
                                textId = R.string.set_default_location,
                                modifier = Modifier
                                    .align(alignment = Alignment.Center)
                                    .widthIn(
                                        min = dimensionResource(
                                            R.dimen.min_location_button_width
                                        )
                                    )
                                    .padding(bottom = dimensionResource(R.dimen.padding_small))
                            )
                        }
                    }
                } else if (weatherAppUiState.weatherLoadError != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(dimensionResource(R.dimen.weather_screen_spacer_height)))
                        Text(
                            text = weatherAppUiState.weatherLoadError,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherScreenTopBar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    WeatherAppButton(
        onClick = onClick,
        imageVector = Icons.Default.Search,
        contentDescriptionId = R.string.choose_city,
        textId = R.string.choose_city,
        modifier = modifier
    )
}

@Composable
fun CurrentWeather(
    weatherAppUiState: WeatherAppUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(
                R.string.current_weather_in_city,
                weatherAppUiState.currentCity!!.name
            ),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier
                .widthIn(max = dimensionResource(R.dimen.max_current_weather_card_width))
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = dimensionResource(R.dimen.card_medium_elevation)
            ),
            shape = RoundedCornerShape(
                dimensionResource(
                    R.dimen.app_components_rounded_corner_shape
                )
            )
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(
                            R.string.temp_in_celsius,
                            weatherAppUiState.currentWeather!!.temp_c.roundToInt()
                        ),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
                    CoilAsyncImage(
                        url = stringResource(
                            R.string.url_coil_format,
                            weatherAppUiState.currentWeather.condition.icon
                        ),
                        contentDescription = stringResource(R.string.current_weather_icon),
                        modifier = Modifier
                            .height(dimensionResource(R.dimen.weather_icon_size))
                            .width(dimensionResource(R.dimen.weather_icon_size))
                    )
                }
                Column {
                    Text(
                        text = weatherAppUiState.currentWeather!!.condition.text
                    )
                    Text(
                        text = stringResource(
                            R.string.wind_speed_in_met_per_sec,
                            fromKilPerHourToMetPerSec(weatherAppUiState.currentWeather.wind_kph)
                        )
                    )
                    Text(
                        text = stringResource(
                            R.string.humidity_in_perc,
                            weatherAppUiState.currentWeather.humidity
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ForecastDayItem(
    forecastDay: ForecastDay,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_to_small_card_elevation)),
        shape = RoundedCornerShape(
            dimensionResource(
                R.dimen.app_components_rounded_corner_shape
            )
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_small_elevation)
        ),
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Text(text = formatDateToRussian(forecastDay.date))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        R.string.temp_in_celsius, fromMaxAndMinTempToAvg(
                            forecastDay.day.maxtemp_c, forecastDay.day.mintemp_c
                        )
                    )
                )
                CoilAsyncImage(
                    url = stringResource(R.string.url_coil_format, forecastDay.day.condition.icon),
                    contentDescription = stringResource(R.string.current_weather_icon)
                )
            }
        }
    }
}