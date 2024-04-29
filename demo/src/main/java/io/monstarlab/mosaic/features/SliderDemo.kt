package io.monstarlab.mosaic.features

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.monstarlab.mosaic.slider.Slider
import io.monstarlab.mosaic.slider.SliderColors
import io.monstarlab.mosaic.slider.distribution.SliderValueDistribution
import io.monstarlab.mosaic.slider.distribution.CheckPointsValueDistribution
import kotlin.math.roundToInt
import androidx.compose.material3.Slider as MaterialSlider

@Composable
fun SliderDemo() = Scaffold(modifier = Modifier) {
    Column(
        modifier = Modifier
            .padding(it)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        MosaicSliderDemo()
    }
}

@Composable
fun MosaicSliderDemo() {
    Column {
        val colors = SliderColors(
            activeTrackColor = Color.Black,
            disabledRangeTrackColor = Color.Red,
            thumbColor = Color.Yellow,
        )
        var enabled by remember { mutableStateOf(true) }
        var isCustom by remember { mutableStateOf(false) }
        var linearDistribution by remember { mutableStateOf(false) }
        var sliderValue by remember { mutableFloatStateOf(500f) }

        MaterialSlider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            valueRange = 0f..1000f,
        )

        val modifier = if (isCustom) {
            Modifier
                .clip(RoundedCornerShape(16.dp))
                .height(15.dp)
        } else {
            Modifier
        }

        val fragmentedDistribution: SliderValueDistribution = remember {
            CheckPointsValueDistribution(
                listOf(
                    0f to 0f,
                    0.2f to 500f,
                    0.4f to 800f,
                    1f to 1000f,
                ),
            )
        }

        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            modifier = modifier,
            enabled = enabled,
            colors = colors,
            range = 0f..1000f,
            disabledRange = 50f..300f,
            valueDistribution = if (linearDistribution) {
                SliderValueDistribution.Linear
            } else {
                fragmentedDistribution
            },
            thumb = {
                if (isCustom) {
                    val transition = rememberInfiniteTransition(label = "")
                    val color = transition.animateColor(
                        initialValue = Color.Red,
                        targetValue = Color.Green,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse,
                        ),
                        label = "",
                    )

                    val rotation = transition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(
                            tween(5000, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart,
                        ),
                        label = "",
                    )

                    Box(
                        modifier = Modifier
                            .rotate(rotation.value)
                            .size(48.dp)
                            .background(color.value),
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(colors.thumbColor(enabled), CircleShape),
                    )
                }
            },

        )

        Text(
            text = "Current value: ${sliderValue.roundToInt()}",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
        ) {
            LabeledSwitch(
                label = "Slider enabled",
                checked = enabled,
                onValueChange = { enabled = it },
            )

            LabeledSwitch(
                label = "Customise",
                checked = isCustom,
                onValueChange = { isCustom = it },
            )
        }

        LabeledSwitch(
            label = "Use linear distribution or parabolic",
            checked = linearDistribution,
            onValueChange = { linearDistribution = it },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
private fun LabeledSwitch(
    label: String,
    checked: Boolean,
    onValueChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Text(text = label, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(3.dp))
        Switch(
            checked = checked,
            onCheckedChange = onValueChange,
        )
    }
}

@Preview
@Composable
private fun PreviewSliderDemo() {
    SliderDemo()
}
