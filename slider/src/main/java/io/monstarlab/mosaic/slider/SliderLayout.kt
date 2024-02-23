package io.monstarlab.mosaic.slider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
internal fun SliderLayout(
    progress: Float,
    track: @Composable () -> Unit,
    thumb: @Composable () -> Unit
) {
    Layout(
        content = {
            Box(modifier = Modifier.layoutId(SliderLayoutElements.Track)) {
                track()
            }
            Box(modifier = Modifier.layoutId(SliderLayoutElements.Thumb)) {
                thumb()
            }
        }) { mesuarables, constraints ->

        val trackPlaceable = mesuarables
            .first { it.layoutId == SliderLayoutElements.Track }
            .measure(constraints)

        val thumbPlaceable = mesuarables
            .first { it.layoutId == SliderLayoutElements.Thumb }
            .measure(constraints)

        val sliderHeight = max(trackPlaceable.height, thumbPlaceable.height)
        val sliderWidth = trackPlaceable.width
        layout(sliderWidth, sliderHeight) {
            val trackY = sliderHeight / 2 - trackPlaceable.height / 2
            val thumbY = sliderHeight / 2 - thumbPlaceable.height / 2
            val thumbX = (sliderWidth * progress).roundToInt() - thumbPlaceable.width / 2
            trackPlaceable.place(0, trackY)
            thumbPlaceable.place(thumbX, thumbY)
        }
    }
}


internal enum class SliderLayoutElements {
    Track, Thumb
}


@Preview
@Composable
private fun PreviewSliderLayout() {
    SliderLayout(
        progress = 0.5f,
        track = {
            Box(
                modifier = Modifier
                    .background(Color.Yellow)
                    .fillMaxWidth()
                    .height(20.dp)
            )
        },
        thumb = {
            Box(modifier = Modifier
                .background(Color.Blue, shape = CircleShape)
                .size(50.dp))
        })
}