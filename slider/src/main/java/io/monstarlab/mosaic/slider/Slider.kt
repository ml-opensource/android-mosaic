package io.monstarlab.mosaic.slider

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection

/**
 * A composable function that creates a slider UI component.
 *
 * @param value the current value of the slider
 * @param onValueChange a callback function invoked when the slider value changes
 * @param colors the colors used to customize the appearance of the slider
 * @param modifier the modifier to be applied to the slider
 * @param valueDistribution the strategy for distributing slider values
 * @param range the range of values the slider can represent
 * @param interactionSource the interaction source used to handle user input interactions
 * @param thumb the composable function used to render the slider thumb
 */
@Composable
public fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    colors: SliderColors,
    modifier: Modifier = Modifier,
    valueDistribution: SliderValueDistribution = SliderValueDistribution.Linear,
    range: ClosedFloatingPointRange<Float> = 0f..1f,
    disabledRange: ClosedFloatingPointRange<Float> = EmptyRange,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumb: @Composable (SliderState) -> Unit = { DefaultSliderThumb(colors = colors) },
) {
    val state = rememberSliderState(value, range, valueDistribution, disabledRange)

    state.onValueChange = onValueChange
    state.value = value

    Slider(
        state = state,
        interactionSource = interactionSource,
        modifier = modifier,
        thumb = thumb,
        colors = colors,
    )
}

/**
 * A composable function that creates a slider UI component.
 * @param state of the Slider where the latest slider value is stored
 * @param colors the colors used to customize the appearance of the slider
 * @param modifier the modifier to be applied to the slider
 * @param interactionSource the interaction source used to handle user input interactions
 * @param thumb the composable function used to render the slider thumb
 */
@Composable
public fun Slider(
    state: SliderState,
    colors: SliderColors,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumb: @Composable (SliderState) -> Unit,
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    val tap = Modifier.pointerInput(state, interactionSource) {
        detectTapGestures(
            onPress = { state.handlePress(it) },
        )
    }

    val drag = Modifier.draggable(
        state = state,
        orientation = Orientation.Horizontal,
        enabled = true,
        interactionSource = interactionSource,
        startDragImmediately = state.isDragging,
        reverseDirection = isRtl,
        onDragStopped = {},
    )

    SliderLayout(
        modifier = modifier
            .focusable(true, interactionSource)
            .then(tap)
            .then(drag),
        thumb = thumb,
        track = {
            SliderTrack(
                progress = state.valueAsFraction,
                colors = colors,
                disabledRange = state.disabledRangeAsFractions,
            )
        },
        state = state,
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

internal val EmptyRange = 0f..0f

@Preview
@Composable
private fun PreviewSlider() {
    Slider(
        value = 0.5f,
        onValueChange = {},
        colors = SliderColors(Color.Yellow),
        disabledRange = 0.8f..1f,
    )
}
