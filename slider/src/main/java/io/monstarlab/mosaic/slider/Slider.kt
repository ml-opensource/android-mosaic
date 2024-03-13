package io.monstarlab.mosaic.slider

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection

@Composable
public fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    colors: SliderColors,
    modifier: Modifier = Modifier,
    valueDistribution: SliderValueDistribution = SliderValueDistribution.Linear,
    range: ClosedFloatingPointRange<Float> = 0f..1f,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumb: @Composable () -> Unit = { DefaultSliderThumb(colors = colors) },
) {
    val state = rememberSliderState(value, valueDistribution)
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    state.value = value
    state.range = range
    state.onValueChange = onValueChange

    val draggable = modifier.draggable(
        state = state,
        orientation = Orientation.Horizontal,
        enabled = true,
        interactionSource = interactionSource,
        startDragImmediately = true,
        reverseDirection = isRtl,
    )

    SliderLayout(
        modifier = modifier.then(draggable),
        thumb = thumb,
        track = {
            SliderTrack(
                progress = state.valueAsFraction,
                colors = colors,
            )
        },
        onDimensionsResolved = state::updateDimensions,
        value = state.valueAsFraction,
    )
}

@Composable
internal fun DefaultSliderThumb(colors: SliderColors) {
    Box(
        modifier = Modifier
            .size(SliderDefaults.ThumbSize)
            .background(
                color = colors.active,
                shape = CircleShape,
            ),
    )
}

@Preview
@Composable
private fun PreviewSlider() {
    Slider(
        value = 0.5f,
        onValueChange = {},
        colors = SliderColors(Color.Yellow),
    )
}
