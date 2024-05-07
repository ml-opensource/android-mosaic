package io.monstarlab.mosaic.demos

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import io.monstarlab.mosaic.slider.MosaicSlider
import io.monstarlab.mosaic.slider.MosaicSliderColors
import io.monstarlab.mosaic.slider.distribution.SliderValuesDistribution
import kotlin.math.roundToInt
import androidx.compose.material3.Slider as MaterialSlider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderDemo() = Scaffold(
    modifier = Modifier,
    topBar = {
        TopAppBar(
            title = { Text(text = "Mosaic - Slider") },
        )
    },
) {
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
    Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
        val colors = MosaicSliderColors(
            activeTrackColor = Color.Black,
            disabledRangeTrackColor = Color.Red,
            thumbColor = Color.Yellow,
        )
        var enabled by remember { mutableStateOf(true) }
        var isCustom by remember { mutableStateOf(false) }
        var nonLinearDistribution by remember { mutableStateOf(false) }
        var sliderValue by remember { mutableFloatStateOf(500f) }
        var disableSubRange by remember { mutableStateOf(false) }

        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier,
        ) {
            MaterialSlider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                valueRange = 0f..1000f,
            )
            Text(text = "Material slider")
        }

        val modifier = if (isCustom) {
            Modifier
                .clip(RoundedCornerShape(16.dp))
                .height(15.dp)
        } else {
            Modifier
        }

        val fragmentedDistribution: SliderValuesDistribution = remember {
            SliderValuesDistribution.checkpoints(
                0f to 0f,
                0.2f to 500f,
                0.4f to 800f,
                1f to 1000f,
            )
        }

        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier,
        ) {
            MosaicSlider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                modifier = modifier,
                enabled = enabled,
                colors = colors,

                range = 0f..1000f,
                disabledRange = if (disableSubRange) 800f..1000f else 0f..0f,
                valueDistribution = if (nonLinearDistribution) {
                    fragmentedDistribution
                } else {
                    SliderValuesDistribution.Linear
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
                                .size(20.dp)
                                .background(colors.thumbColor(enabled), CircleShape),
                        )
                    }
                },
            )
            Text(text = "Mosaic slider")
        }

        Text(
            text = "Current value: ${sliderValue.roundToInt()}",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth(),
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
                alignment = Alignment.End,

            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
        ) {
            LabeledSwitch(
                label = "Use Non-Linear distribution",
                checked = nonLinearDistribution,
                onValueChange = { nonLinearDistribution = it },
            )

            LabeledSwitch(
                label = "Disable sub-range",
                checked = disableSubRange,
                onValueChange = { disableSubRange = it },
                alignment = Alignment.End,
            )
        }
    }
}

@Composable
private fun LabeledSwitch(
    label: String,
    checked: Boolean,
    onValueChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = alignment,

    ) {
        Text(
            text = label,
            textAlign = TextAlign.Start,
        )
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
