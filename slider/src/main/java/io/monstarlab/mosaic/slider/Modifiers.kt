package io.monstarlab.mosaic.slider

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.progressSemantics
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress

internal fun Modifier.sliderDragModifier(
    state: SliderState,
    enabled: Boolean,
    interactionSource: MutableInteractionSource,
    isRtl: Boolean,
): Modifier = this.draggable(
    state = state,
    orientation = Orientation.Horizontal,
    enabled = enabled,
    interactionSource = interactionSource,
    startDragImmediately = state.isDragging,
    reverseDirection = isRtl
)

internal fun Modifier.sliderTapModifier(
    state: SliderState,
    enabled: Boolean,
    interactionSource: MutableInteractionSource,
): Modifier {
    return if (enabled) {
        this.pointerInput(state, interactionSource) {
            detectTapGestures(
                onPress = { state.handlePress(it) },
            )
        }
    } else {
        this
    }
}

internal fun Modifier.sliderSemantics(state: SliderState, enabled: Boolean): Modifier {
    return semantics {
        if (!enabled) disabled()
        setProgress(
            action = { targetValue ->
                val coercedValue = state.coerceValue(targetValue)
                // This is to keep it consistent with AbsSeekbar.java: return false if no
                // change from current.
                if (coercedValue == state.value) {
                    false
                } else {
                    if (coercedValue != state.value) {
                        if (state.onValueChange != null) {
                            state.onValueChange?.let {
                                it(coercedValue)
                            }
                        } else {
                            state.value = coercedValue
                        }
                    }
                    true
                }
            },
        )
    }.progressSemantics(
        value = state.value,
        valueRange = state.range.start..state.range.endInclusive,
    )
}
