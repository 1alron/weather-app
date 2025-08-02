package com.alron.weatherapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.alron.weatherapp.R
import com.alron.weatherapp.api.City
import com.alron.weatherapp.ui.WeatherAppUiState

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
            .fillMaxWidth()
            .padding(
                top = dimensionResource(R.dimen.padding_small),
                end = dimensionResource(R.dimen.padding_medium),
                start = dimensionResource(R.dimen.padding_medium),
                bottom = dimensionResource(R.dimen.padding_medium),
            )
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                dimensionResource(
                    R.dimen.padding_between_text_field_and_button
                )
            ),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .widthIn(
                    max = dimensionResource(
                        R.dimen.max_text_field_and_list_width
                    )
                )
                .fillMaxWidth()
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
                    .weight(1f)
            )
            IconButton(
                onClick = { onBackButtonClicked() },
                modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button_description),
                )
            }
        }
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))
        LazyColumn(
            modifier = Modifier
                .widthIn(
                    max = dimensionResource(
                        R.dimen.max_text_field_and_list_width
                    )
                )
                .fillMaxWidth()
        ) {
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