package io.monstarlab.mosaic.features

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.monstarlab.mosaic.slider.Slider
import io.monstarlab.mosaic.slider.SliderColors

@Composable
fun SliderDemo() = Scaffold(modifier = Modifier) {
    Column(modifier = Modifier.padding(it).padding(16.dp)) {
        Slider(
            value = 0.5f,
            onValueChange = {},
            colors = SliderColors(Color.Red),
            modifier = Modifier.clip(RoundedCornerShape(2.dp))
        )
    }
}

@Preview
@Composable
private fun PreviewSliderDemo() {
    SliderDemo()
}