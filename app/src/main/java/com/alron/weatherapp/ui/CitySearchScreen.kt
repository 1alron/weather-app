package com.alron.weatherapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.alron.weatherapp.R
import com.alron.weatherapp.api.City

@Composable
fun CitySearchScreen(
    weatherAppUiState: WeatherAppUiState,
    onQueryChange: (String) -> Unit,
    onBackButtonClicked: () -> Unit,
    onCitySelected: (City) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(
                top = dimensionResource(R.dimen.search_screen_padding_top),
                start = dimensionResource(R.dimen.search_screen_padding_sides),
                end = dimensionResource(R.dimen.search_screen_padding_sides)
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                dimensionResource(
                    R.dimen.padding_between_text_field_and_button
                )
            ),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = weatherAppUiState.query,
                onValueChange = onQueryChange,
                shape = RoundedCornerShape(
                    dimensionResource(
                        R.dimen.app_components_rounded_corner_shape
                    )
                ),
                label = {
                    Text(
                        text =
                            stringResource(R.string.input_city)
                    )
                },
                modifier = Modifier
            )
            Box(
                modifier = Modifier
                    .size(dimensionResource(R.dimen.back_button_size))
            ) {
                Button(
                    onClick = { onBackButtonClicked() },
                    contentPadding = PaddingValues(dimensionResource(R.dimen.padding_small)),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button_description)
                    )
                }
            }
        }
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))
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
            text = stringResource(R.string.city_region_and_country, city.region, city.country),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}