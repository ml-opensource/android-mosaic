package io.monstarlab.mosaic.slider

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.offset
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
public fun SliderLayout(
    state: SliderState,
    modifier: Modifier = Modifier,
    track: @Composable () -> Unit,
    thumb: @Composable (SliderState) -> Unit,
) {
    Layout(
        modifier = modifier.requiredSizeIn(SliderDefaults.HandleWidth, SliderDefaults.HandleHeight),
        content = {
            Box(modifier = Modifier.layoutId(SliderLayoutElements.Track)) {
                track()
            }
            Box(modifier = Modifier.layoutId(SliderLayoutElements.Thumb)) {
                thumb(state)
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
        state.updateDimensions(sliderWidth.toFloat(), thumbPlaceable.width.toFloat())

        val trackOffsetX = thumbPlaceable.width / 2
        val thumbOffsetX = ((trackPlaceable.width) * state.valueAsFraction).roundToInt()
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
