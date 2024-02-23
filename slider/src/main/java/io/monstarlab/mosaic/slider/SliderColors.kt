package io.monstarlab.mosaic.slider

import androidx.compose.ui.graphics.Color

public data class SliderColors(
    val active: Color,
    val disabled: Color = active.copy(alpha = 0.2f),
    val inactive: Color = active.copy(alpha = 0.5f),
)