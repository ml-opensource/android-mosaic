package io.monstarlab.mosaic.features

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.monstarlab.mosaic.slider.Slider
import io.monstarlab.mosaic.slider.SliderColors
import io.monstarlab.mosaic.slider.SliderValueDistribution
import io.monstarlab.mosaic.slider.rememberSliderState
import kotlin.math.roundToInt
import androidx.compose.material3.Slider as MaterialSlider

@Composable
fun SliderDemo() = Scaffold(modifier = Modifier) {
    var materialSliderValue by remember { mutableFloatStateOf(50f) }

    Column(
        modifier = Modifier
            .padding(it)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        MaterialSlider(
            value = materialSliderValue,
            onValueChange = { materialSliderValue = it },
            valueRange = 0f..100f,
        )
        Text(text = materialSliderValue.toString())

        MosaicSliderDemo(
            label = "Linear division strategy",
            range = 0f..1000f,
            valuesDistribution = SliderValueDistribution.Linear,
            disabledRange = 0f..500f,
        )
        MosaicSliderDemo(
            label = "Parabolic",
            range = 0f..1000f,
            disabledRange = 800f..1000f,
            valuesDistribution = SliderValueDistribution.parabolic(
                a = (1000 - 100 * 0.1f) / (1000 * 1000),
                b = 0.1f,
                c = 1f,
            ),
        )

        Slider(
            state = rememberSliderState(value = 0.5f),
            colors = SliderColors(Color.Magenta, Color.Red),
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color.Black),
            )
        }

        Slider(
            state = rememberSliderState(value = 0.5f),
            colors = SliderColors(Color.Black),
            enabled = true,
        ) {
            Box(modifier = Modifier.background(Color.Red, shape = CircleShape).size(32.dp))
        }

        var v by remember { mutableFloatStateOf(0.5f) }
        Slider(
            value = v,
            onValueChange = { v = it },
            colors = SliderColors(Color.Black),
            enabled = false,
        ) {
            Box(modifier = Modifier.background(Color.Red, shape = CircleShape).size(32.dp))
        }
    }
}

@Composable
fun MosaicSliderDemo(
    label: String,
    range: ClosedFloatingPointRange<Float>,
    valuesDistribution: SliderValueDistribution,
    modifier: Modifier = Modifier,
    disabledRange: ClosedFloatingPointRange<Float> = 0f..0f,
) {
    Column(modifier) {
        Text(text = label)

        var value by remember { mutableFloatStateOf(50f) }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier,
        ) {
            Text(
                text = range.start.toString(),
                modifier = Modifier.weight(0.2f),
            )
            Slider(
                modifier = Modifier.weight(0.8f),
                value = value,
                onValueChange = { value = it },
                colors = SliderColors(Color.Black, disabledRangeTrackColor = Color.Red),
                range = range,
                valueDistribution = valuesDistribution,
                disabledRange = disabledRange,
                thumb = {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.Yellow),
                    ) {
                        Text(text = value.roundToInt().toString())
                    }
                },
            )
            Text(
                text = range.endInclusive.toString(),
                modifier = Modifier.weight(0.2f),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSliderDemo() {
    SliderDemo()
}
