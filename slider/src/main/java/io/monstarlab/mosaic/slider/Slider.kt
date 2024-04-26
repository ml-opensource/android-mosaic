package io.monstarlab.mosaic.slider

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import io.monstarlab.mosaic.slider.distribution.SliderValueDistribution

/**
 * A composable function that creates a slider UI component.
 * @param value the current value of the slider
 * @param onValueChange a callback function invoked when the slider value changes
 * @param enabled - determines whether the user can interact with the slide or not
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
    enabled: Boolean = true,
    valueDistribution: SliderValueDistribution = SliderValueDistribution.Linear,
    range: ClosedFloatingPointRange<Float> = 0f..1f,
    disabledRange: ClosedFloatingPointRange<Float> = EmptyRange,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumb: @Composable (
        SliderState,
    ) -> Unit = { DefaultSliderThumb(colors = colors, enabled = enabled) },
) {
    val state = rememberSliderState(value, range, valueDistribution, disabledRange)
    state.onValueChange = onValueChange
    state.value = value

    // Workaround for the initial value that might belong into disabled range
    // The initial value is remembered
    val initialValue = remember { mutableFloatStateOf(value) }

    // In case initial value differs from the value inside the state
    // this means the value has been coerced into it
    // for this case the change of value must be explicitly notified to the receiver
    LaunchedEffect(initialValue) {
        if (initialValue.floatValue != state.value) {
            onValueChange(state.value)
        }
    }

    Slider(
        state = state,
        interactionSource = interactionSource,
        modifier = modifier,
        thumb = thumb,
        colors = colors,
        enabled = enabled,
    )
}

/**
 * A composable function that creates a slider UI component.
 * @param state of the Slider where the latest slider value is stored
 * @param colors the colors used to customize the appearance of the slider
 * @param modifier the modifier to be applied to the slider.
 * @param enabled - determines whether the user can interact with the slide or not
 * @param interactionSource the interaction source used to handle user input interactions
 * @param thumb the composable function used to render the slider thumb
 */
@Composable
public fun Slider(
    state: SliderState,
    colors: SliderColors,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumb: @Composable (SliderState) -> Unit,
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    SliderLayout(
        modifier = Modifier
            .sliderSemantics(state, enabled)
            .focusable(enabled, interactionSource)
            .sliderTapModifier(state, enabled, interactionSource)
            .sliderDragModifier(state, enabled, interactionSource, isRtl),
        thumb = thumb,
        track = {
            SliderTrack(
                progress = state.offsetAsFraction,
                colors = colors,
                disabledRange = state.disabledRangeAsFractions,
                enabled = enabled,
                modifier = modifier,
            )
        },
        state = state,
    )
}

@Composable
internal fun DefaultSliderThumb(enabled: Boolean, colors: SliderColors) {
    Box(
        modifier = Modifier
            .size(SliderDefaults.ThumbSize)
            .background(
                color = colors.thumbColor(enabled),
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
        colors = SliderColors(Color.Yellow, Color.Red),
        disabledRange = 0.8f..1f,
    )
}

@Preview
@Composable
private fun PreviewDisabledSlider() {
    Slider(
        value = 0.5f,
        onValueChange = {},
        colors = SliderColors(Color.Yellow, Color.Red),
        disabledRange = 0.8f..1f,
    )
}
