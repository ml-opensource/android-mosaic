package io.monstarlab.mosaic.slider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview

@Composable
public fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    colors: SliderColors,
    modifier: Modifier = Modifier,
    thumb: @Composable () -> Unit = { DefaultSliderThumb(colors = colors) }
) {

    SliderLayout(
        progress = value,
        thumb = thumb,
        track = {
            SliderTrack(
                modifier = modifier,
                progress = value,
                colors = colors,
            )
        }
    )
}

@Composable
internal fun DefaultSliderThumb(colors: SliderColors) {
    Box(
        modifier = Modifier
            .size(SliderDefaults.ThumbSize)
            .background(
                color = colors.active,
                shape = CircleShape
            )
    )
}

@Preview
@Composable
private fun PreviewSlider() {
    Slider(
        value = 0.5f,
        onValueChange = {},
        colors = SliderColors(Color.Yellow)
    )
}