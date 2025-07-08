package com.alron.weatherapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


enum class Routes {
    Weather,
    SearchCities
}

@Composable
fun WeatherApp() {
    val viewModel: WeatherAppViewModel = hiltViewModel()
    val weatherAppUiState = viewModel.uiState.collectAsState().value
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Weather.name
    ) {
        composable(route = Routes.Weather.name) {
            WeatherScreen(
                isShowingWeather = weatherAppUiState.currentCity != null,
                onSearchButtonClicked = {
                    navController.navigate(Routes.SearchCities.name)
                },
            )
        }

        composable(route = Routes.SearchCities.name) {
            CitySearchScreen(
                weatherAppUiState = weatherAppUiState,
                onQueryChange = viewModel::onQueryChange,
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
