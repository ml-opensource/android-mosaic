package io.monstarlab.mosaic.features

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.monstarlab.mosaic.slider.Slider
import io.monstarlab.mosaic.slider.SliderColors
import androidx.compose.material3.Slider as MaterialSlider
@Composable
fun SliderDemo() = Scaffold(modifier = Modifier) {

    var materialSliderValue by remember { mutableFloatStateOf(50f) }
    var mosaicSliderValue by remember { mutableFloatStateOf(50f) }
    

    Column(modifier = Modifier
        .padding(it)
        .padding(16.dp)
        .background(Color.LightGray)) {

        MaterialSlider(
            value = materialSliderValue,
            onValueChange = { materialSliderValue = it},
            valueRange = 0f..100f
        )
        Text(text = materialSliderValue.toString())

        Spacer(modifier = Modifier.size(32.dp))

        Slider(
            value = mosaicSliderValue,
            onValueChange = { mosaicSliderValue = it },
            colors = SliderColors(Color.Red),
            modifier = Modifier.clip(RoundedCornerShape(2.dp)),
            range = 0f..100f
        ) {
            Box(modifier = Modifier.background(Color.Yellow).size(32.dp))
        }
        Text(text = mosaicSliderValue.toString())


    }
}

@Preview
@Composable
private fun PreviewSliderDemo() {
    SliderDemo()
}
