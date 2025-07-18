package com.alron.weatherapp.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alron.weatherapp.ui.animations.enterSlideInFromLeft
import com.alron.weatherapp.ui.animations.enterSlideInFromRight
import com.alron.weatherapp.ui.animations.exitSlideOutToLeft
import com.alron.weatherapp.ui.animations.exitSlideOutToRight


enum class Routes {
    Weather,
    SearchCities
}

@Composable
fun WeatherApp() {
    val viewModel: WeatherAppViewModel = hiltViewModel()
    val weatherAppUiState = viewModel.uiState.collectAsState().value
    val navController: NavHostController = rememberNavController()
    val context = LocalContext.current

    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val defaultCity = viewModel.getDefaultCity()
        startDestination = if (defaultCity != null) {
            viewModel.onCitySelected(defaultCity)
            Routes.Weather.name
        } else {
            Routes.SearchCities.name
        }
    }

    startDestination?.let { dest ->
        NavHost(
            navController = navController,
            startDestination = dest
        ) {
            composable(
                route = Routes.Weather.name,
                enterTransition = { enterSlideInFromRight() },
                exitTransition = { exitSlideOutToLeft() },
                popEnterTransition = { enterSlideInFromLeft() },
                popExitTransition = { exitSlideOutToRight() }
            ) {
                WeatherScreen(
                    weatherAppUiState = weatherAppUiState,
                    onSearchButtonClicked = {
                        navController.navigate(Routes.SearchCities.name)
                    },
                    onSetDefaultLocation = {
                        viewModel.setDefaultLocation(
                            city = weatherAppUiState.currentCity!!,
                            weather = weatherAppUiState.currentWeather!!,
                            forecast = weatherAppUiState.forecast
                        )

                    },
                    onRefresh = {
                        if (weatherAppUiState.currentCity != null) {
                            viewModel.loadWeather(weatherAppUiState.currentCity.name)
                        }
                    }
                )
            }

            composable(
                route = Routes.SearchCities.name,
                enterTransition = { enterSlideInFromLeft() },
                exitTransition = { exitSlideOutToRight() },
                popEnterTransition = { enterSlideInFromRight() },
                popExitTransition = { exitSlideOutToLeft() }
            ) {
                CitySearchScreen(
                    weatherAppUiState = weatherAppUiState,
                    onQueryChange = viewModel::onQueryChange,
                    onBackButtonClicked = {
                        if (!navController.popBackStack()) {
                            (context as? Activity)?.finish()
                        }
                    },
                    onCitySelected = { city ->
                        navController.navigate(Routes.Weather.name) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                        viewModel.onCitySelected(city)
                    }
                )
            }
        }
    }
}
