package com.alron.weatherapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alron.weatherapp.R
import com.alron.weatherapp.api.model.ForecastDay
import com.alron.weatherapp.ui.CoilAsyncImage
import com.alron.weatherapp.ui.WeatherAppUiState
import com.alron.weatherapp.ui.buttons.DefaultCityButton
import com.alron.weatherapp.ui.buttons.WeatherAppButton
import com.alron.weatherapp.ui.text.TitleText
import com.alron.weatherapp.util.NUMBER_OF_DAYS_WITH_FORECAST
import com.alron.weatherapp.util.formatDateToRussian
import com.alron.weatherapp.util.fromKilPerHourToMetPerSec
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun WeatherScreen(
    weatherAppUiState: WeatherAppUiState,
    onSearchButtonClicked: () -> Unit,
    onSetDefaultLocation: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val swipeState = rememberSwipeRefreshState(
        isRefreshing = weatherAppUiState.isLoadingWeatherAndForecast
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        SwipeRefresh(
            state = swipeState,
            onRefresh = { onRefresh() },
            modifier = modifier
                .padding(padding)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(
                        top = dimensionResource(R.dimen.padding_small),
                        start = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_medium),
                        bottom = dimensionResource(R.dimen.padding_medium)
                    )
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .widthIn(max = dimensionResource(R.dimen.max_weather_card_width))
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            WeatherScreenTopBar(
                                onClick = onSearchButtonClicked,
                                modifier = Modifier
                            )
                        }
                    }
                    if (weatherAppUiState.currentCity == null) {
                        item {
                            Spacer(Modifier.height(dimensionResource(R.dimen.weather_screen_spacer_height)))
                            Text(
                                text = stringResource(R.string.start_text),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                            )
                        }
                    } else if (weatherAppUiState.currentWeather != null &&
                        weatherAppUiState.forecast.isNotEmpty()
                    ) {
                        item {
                            Column {
                                Spacer(Modifier.height(dimensionResource(R.dimen.weather_screen_spacer_height)))
                                DefaultCityButton(
                                    onClick = {
                                        onSetDefaultLocation()
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = "Данные о текущем городе сохранены",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    },
                                )
                                Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))
                                CurrentWeather(
                                    weatherAppUiState = weatherAppUiState,
                                )
                                Spacer(Modifier.height(dimensionResource(R.dimen.weather_screen_spacer_height)))
                                TitleText(
                                    text = stringResource(
                                        R.string.weather_forecast_some_days,
                                        NUMBER_OF_DAYS_WITH_FORECAST
                                    )
                                )
                                Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))
                            }
                        }
                        itemsIndexed(weatherAppUiState.forecast) { index, forecastDay ->
                            Column {
                                ForecastDayItem(forecastDay)
                                Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))
                            }
                        }
                    } else if (weatherAppUiState.weatherLoadError != null) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(
                                    Modifier.height(
                                        dimensionResource(R.dimen.weather_screen_spacer_height)
                                    )
                                )
                                Text(
                                    text = weatherAppUiState.weatherLoadError,
                                )
                            }
                        }
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
            .widthIn(max = 400.dp)
            .fillMaxWidth()
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
        TitleText(
            text = stringResource(
                R.string.current_weather_in_city,
                weatherAppUiState.currentCity!!.name
            )
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier
                .widthIn(max = dimensionResource(R.dimen.max_weather_card_width))
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = dimensionResource(R.dimen.card_small_elevation)
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
    var expanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
            .widthIn(max = dimensionResource(R.dimen.max_weather_card_width))
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(
            dimensionResource(
                R.dimen.app_components_rounded_corner_shape
            )
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_small_elevation)
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(R.dimen.forecast_card_padding))
            ) {
                Text(
                    text = formatDateToRussian(forecastDay.date),
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(
                            R.string.temp_in_celsius, forecastDay.day.maxtemp_c.roundToInt()
                        )
                    )
                    CoilAsyncImage(
                        url = stringResource(
                            R.string.url_coil_format,
                            forecastDay.day.condition.icon
                        ),
                        contentDescription = stringResource(R.string.current_weather_icon)
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = dimensionResource(R.dimen.padding_medium))
                    .rotate(rotationAngle)
            )
        }
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        start = dimensionResource(R.dimen.forecast_card_padding),
                        end = dimensionResource(R.dimen.forecast_card_padding),
                        bottom = dimensionResource(R.dimen.forecast_card_padding)
                    )
                    .fillMaxWidth()
            ) {
                Text(text = forecastDay.day.condition.text)
                Text(
                    text = stringResource(
                        R.string.max_wind_speed_in_met_per_sec, fromKilPerHourToMetPerSec(
                            forecastDay.day.maxwind_kph
                        )
                    )
                )
                Text(text = stringResource(R.string.humidity_in_perc, forecastDay.day.avghumidity))
            }
        }
    }
}