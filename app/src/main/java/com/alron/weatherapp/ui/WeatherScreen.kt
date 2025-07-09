package com.alron.weatherapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.alron.weatherapp.R
import com.alron.weatherapp.util.fromKilPerHourToMetPerSec
import kotlin.math.roundToInt

@Composable
fun WeatherScreen(
    weatherAppUiState: WeatherAppUiState,
    onSearchButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .padding(start = dimensionResource(R.dimen.padding_medium)),
        contentPadding = WindowInsets.safeDrawing.asPaddingValues(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        item {
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
        }
        if (weatherAppUiState.currentCity == null) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = stringResource(R.string.start_text),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        } else if (weatherAppUiState.currentWeather != null && weatherAppUiState.forecast != null) {
            item {
                Column {
                    Spacer(Modifier.height(dimensionResource(R.dimen.weather_screen_spacer_height)))
                    CurrentWeather(
                        weatherAppUiState = weatherAppUiState
                    )
                }
            }
        } else {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = stringResource(R.string.weather_not_found),
                        modifier = Modifier.align(Alignment.Center)
                    )
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
    OutlinedButton(
        onClick = onClick,
        contentPadding = PaddingValues(
            start = dimensionResource(R.dimen.outlined_button_content_start_padding),
            end = dimensionResource(R.dimen.outlined_button_content_end_padding)
        )
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(R.string.choose_city)
        )
        Spacer(Modifier.width(dimensionResource(R.dimen.padding_medium)))
        Text(
            text = stringResource(R.string.choose_city)
        )
    }
}

@Composable
fun CurrentWeather(
    weatherAppUiState: WeatherAppUiState,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = stringResource(
                R.string.current_weather_in_city,
                weatherAppUiState.currentCity!!.name
            ),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
        Card {
            Column(
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            ) {
                Row {
                    Text(
                        text = stringResource(
                            R.string.temp_in_celsius,
                            weatherAppUiState.currentWeather!!.temp_c.roundToInt()
                        )
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