package io.monstarlab.mosaic.slider

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.gestures.DragScope
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.coroutineScope

public class SliderState(
    value: Float,
    private val valueDistribution: SliderValueDistribution,
) : DraggableState {

    internal var range: ClosedFloatingPointRange<Float> = 0f..1f
    internal var onValueChange: ((Float) -> Unit)? = null
    internal var isDragging by mutableStateOf(false)
        private set

    private var totalWidth by mutableFloatStateOf(0f)
    private var thumbWidth by mutableFloatStateOf(0f)

    private var rawOffset by mutableFloatStateOf(scaleToOffset(value))
    private val scrollMutex = MutatorMutex()

    internal val valueAsFraction: Float get() {
        return calcFraction(0f, totalWidth, rawOffset)
    }

    public var value: Float
        get() = scaleToUserValue(rawOffset)
        set(value) {
            rawOffset = scaleToOffset(value)
        }

    private val dragScope: DragScope = object : DragScope {
        override fun dragBy(pixels: Float): Unit = dispatchRawDelta(pixels)
    }

    override fun dispatchRawDelta(delta: Float) {
        val newRawOffset = rawOffset + delta
        val userValue = scaleToUserValue(newRawOffset)
        if (userValue != value) {
            if (onValueChange != null) {
                onValueChange?.invoke(userValue)
            } else {
                rawOffset = newRawOffset
            }
        }
    }

    override suspend fun drag(
        dragPriority: MutatePriority,
        block: suspend DragScope.() -> Unit,
    ): Unit = coroutineScope {
        isDragging = true
        scrollMutex.mutateWith(dragScope, dragPriority, block)
        isDragging = false
    }

    internal fun updateDimensions(totalWidth: Float, thumbWidth: Float) {
        this.totalWidth = totalWidth
        this.thumbWidth = thumbWidth
    }

    private fun scaleToUserValue(offset: Float): Float {
        val rangeStart = valueDistribution.interpolate(range.start)
        val rangeEnd = valueDistribution.interpolate(range.endInclusive)
        val scaledUserValue = scale(0f, totalWidth, offset, rangeStart, rangeEnd)
        return valueDistribution.inverse(scaledUserValue)
    }

    private fun scaleToOffset(value: Float): Float {
        val rangeStart = valueDistribution.interpolate(range.start)
        val rangeEnd = valueDistribution.interpolate(range.endInclusive)
        val interpolated = valueDistribution.interpolate(value)
        return scale(rangeStart, rangeEnd, interpolated, 0f, totalWidth)
    }
}

@Composable
public fun rememberSliderState(
    value: Float,
    valueDistribution: SliderValueDistribution = SliderValueDistribution.Linear,
): SliderState {
    return remember { SliderState(value, valueDistribution) }
}
