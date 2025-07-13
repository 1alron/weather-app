package com.alron.weatherapp.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.alron.weatherapp.R

@Composable
fun WeatherAppButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    @StringRes contentDescriptionId: Int,
    @StringRes textId: Int,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        contentPadding = PaddingValues(
            start = dimensionResource(R.dimen.outlined_button_content_start_padding),
            end = dimensionResource(R.dimen.outlined_button_content_end_padding)
        ),
        shape = RoundedCornerShape(
            dimensionResource(
                R.dimen.app_components_rounded_corner_shape
            )
        ),
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(contentDescriptionId)
        )
        Spacer(Modifier.width(dimensionResource(R.dimen.padding_medium)))
        Text(
            text = stringResource(textId)
        )
    }
}