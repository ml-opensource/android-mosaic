package io.monstarlab.mosaic.slider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
public fun SliderLayout(
    value: Float,
    onDimensionsResolved: (Float, Float) -> Unit,
    modifier: Modifier = Modifier,
    track: @Composable () -> Unit,
    thumb: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier.requiredSizeIn(SliderDefaults.HandleWidth, SliderDefaults.HandleHeight),
        content = {
            Box(modifier = Modifier.layoutId(SliderLayoutElements.Track)) {
                track()
            }
            Box(modifier = Modifier.layoutId(SliderLayoutElements.Thumb)) {
                thumb()
            }
        },
    ) { mesuarables, constraints ->

        val thumbPlaceable = mesuarables
            .first { it.layoutId == SliderLayoutElements.Thumb }
            .measure(constraints)

        val trackPlaceable = mesuarables
            .first { it.layoutId == SliderLayoutElements.Track }
            .measure(
                constraints.offset(
                    horizontal = -thumbPlaceable.width,
                ).copy(minHeight = 0),
            )

        val sliderHeight = max(thumbPlaceable.height, trackPlaceable.height)
        val sliderWidth = trackPlaceable.width + thumbPlaceable.width
        onDimensionsResolved(sliderWidth.toFloat(), thumbPlaceable.width.toFloat())

        val trackOffsetX = thumbPlaceable.width / 2
        val thumbOffsetX = ((trackPlaceable.width) * value).roundToInt()
        val trackOffsetY = (sliderHeight - trackPlaceable.height) / 2
        val thumbOffsetY = (sliderHeight - thumbPlaceable.height) / 2

        layout(sliderWidth, sliderHeight) {
            trackPlaceable.placeRelative(
                trackOffsetX,
                trackOffsetY,
            )
            thumbPlaceable.placeRelative(
                thumbOffsetX,
                thumbOffsetY,
            )
        }
    }
}

internal enum class SliderLayoutElements {
    Track,
    Thumb,
}

@Preview
@Composable
private fun PreviewSliderLayout() {
    Box(modifier = Modifier.background(Color.Red)) {
        SliderLayout(
            onDimensionsResolved = { _, _ -> },
            modifier = Modifier.fillMaxWidth(0.5f),
            value = 1f,
            track = {
                Box(
                    modifier = Modifier
                        .background(Color.Yellow)
                        .height(12.dp)
                        .fillMaxWidth(),
                )
            },
            thumb = {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.Black, shape = CircleShape),
                )
            },
        )
    }
}