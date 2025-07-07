package com.alron.weatherapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.alron.weatherapp.R
import com.alron.weatherapp.api.City

@Composable
fun CitySearchScreen(
    weatherAppUiState: WeatherAppUiState,
    onQueryChange: (String) -> Unit,
    onCitySelected: (City) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.search_screen_padding))
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
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))
        if (weatherAppUiState.currentCity == null) {
            LazyColumn {
                items(weatherAppUiState.cityList) { city ->
                    CityItem(
                        city = city,
                        onClick = { onCitySelected(city) }
                    )
                }
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
            .clickable { onClick() }
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