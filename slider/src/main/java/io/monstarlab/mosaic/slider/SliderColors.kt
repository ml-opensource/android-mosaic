package io.monstarlab.mosaic.slider

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Immutable
public class SliderColors(
    public val activeTrackColor: Color,
    public val disabledRangeTrackColor: Color = activeTrackColor,
    public val inactiveTrackColor: Color = activeTrackColor.copy(alpha = 0.5f),
    public val disabledActiveTrackColor: Color = activeTrackColor.copy(alpha = 0.2f),
    public val disabledInactiveTrackColor: Color = activeTrackColor.copy(alpha = 0.2f),
    public val thumbColor: Color = activeTrackColor,
    public val disabledThumbColor: Color = disabledRangeTrackColor,
) {
    @Stable
    public fun activeTrackColor(enabled: Boolean): Color {
        return if (enabled) {
            activeTrackColor
        } else {
            disabledActiveTrackColor
        }
    }

    @Stable
    public fun thumbColor(enabled: Boolean): Color {
        return if (enabled) {
            thumbColor
        } else {
            disabledThumbColor
        }
    }

    @Stable
    public fun inactiveTrackColor(enabled: Boolean): Color {
        return if (enabled) {
            inactiveTrackColor
        } else {
            disabledInactiveTrackColor
        }
    }
}
