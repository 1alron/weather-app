package com.alron.weatherapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alron.weatherapp.api.City
import com.alron.weatherapp.R
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.stringResource


@Composable
fun WeatherApp() {
    val viewModel: WeatherAppViewModel = viewModel()
    val weatherAppUiState = viewModel.uiState.collectAsState().value

    CitySearchScreen(
        weatherAppUiState = weatherAppUiState,
        onQueryChange = viewModel::onQueryChange,
    )
}

@Composable
fun CitySearchScreen(
    weatherAppUiState: WeatherAppUiState,
    onQueryChange: (String) -> Unit,
    //onCitySelected: (City) -> Unit
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = weatherAppUiState.query,
            onValueChange = onQueryChange,
            label = {
                Text(
                    text =
                        stringResource(R.string.input_city)
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))
        LazyColumn {
            items(weatherAppUiState.cityList) { city ->
                CityItem(
                    city = city,
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun CityItem(
    city: City,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick }
            .padding(dimensionResource(R.dimen.padding_small))
    ) {
        Text(
            text = city.name,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "${city.region}, ${city.country}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}