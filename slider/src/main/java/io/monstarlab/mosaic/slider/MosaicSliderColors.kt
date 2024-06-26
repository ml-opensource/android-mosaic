package io.monstarlab.mosaic.slider

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

/**
 * Colors of the slider
 */
@Immutable
public class MosaicSliderColors(
    /**
     * Color of the active track representing the track to the start of the thumb
     */
    private val activeTrackColor: Color,
    /**
     * Represents the color of the disabled range,
     * where user is not able to put a thumb to
     */
    internal val disabledRangeTrackColor: Color = activeTrackColor,
    /**
     * Color of the Inactive track, to the end of the thumb
     */
    private val inactiveTrackColor: Color = activeTrackColor.copy(alpha = 0.5f),
    /**
     * Active track color when the Slider is disabled and user can't interact with it
     */
    private val disabledActiveTrackColor: Color = activeTrackColor.copy(alpha = 0.2f),
    /**
     * Color of the Inactive track when the Slider is disabled
     */
    private val disabledInactiveTrackColor: Color = activeTrackColor.copy(alpha = 0.2f),
    /**
     * Color of the thumb
     * only applied when the default thumb is used
     */
    private val thumbColor: Color = activeTrackColor,
    /**
     * Color of the thumb when the Slider is disabled
     * Only applied when the default thumb is used
     */
    private val disabledThumbColor: Color = inactiveTrackColor,
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
