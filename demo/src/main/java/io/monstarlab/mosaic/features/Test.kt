package io.monstarlab.mosaic.features

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.monstarlab.mosaic.slider.SliderColors
import io.monstarlab.mosaic.slider.distribution.SliderValuesDistribution
import io.monstarlab.mosaic.ui.theme.MosaicTheme
import kotlin.math.roundToInt
import androidx.compose.material3.Slider as MaterialSlider
import io.monstarlab.mosaic.slider.Slider as MosaicSlider

@Composable
@Preview()
fun Test() {
    MosaicTheme {

        Surface {
            var value by remember { mutableStateOf(50f) }
            val range = 0f..100f

            Column(Modifier.padding(16.dp)) {

                Text(
                    "Sliders with different distributions\nValue: ${value.roundToInt()}",
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.padding(8.dp))

                Text(
                    "Linear distribution",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp
                )
                MosaicSlider(
                    value = value,
                    onValueChange = { value = it },
                    colors = SliderColors(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.error
                    ),
                    range = range,
                )

                Text(
                    "Non-linear distribution",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp
                )

                MosaicSlider(
                    value = value,
                    onValueChange = { value = it },
                    colors = SliderColors(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.error
                    ),
                    range = range,
                    valueDistribution = remember {
                        SliderValuesDistribution.checkpoints(
                            0f to 0f,
                            0.5f to 20f,
                            1f to 100f
                        )
                    }
                )
            }
        }

    }
}