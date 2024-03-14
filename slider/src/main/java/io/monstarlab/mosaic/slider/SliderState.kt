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
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.coroutineScope

/**
 * A compose state for the Slider component
 * Responsible for managing internal properties such as offset value and drag / click behaviours
 */
public class SliderState(
    value: Float,
    disabledRange: ClosedFloatingPointRange<Float>,
    private val valueDistribution: SliderValueDistribution,
) : DraggableState {

    internal var range: ClosedFloatingPointRange<Float> = 0f..1f
    internal var disabledRange by mutableStateOf(disabledRange)
    internal var onValueChange: ((Float) -> Unit)? = null
    internal var isDragging by mutableStateOf(false)
        private set
    private var totalWidth by mutableFloatStateOf(0f)
    private var thumbWidth by mutableFloatStateOf(0f)

    private var rawOffset by mutableFloatStateOf(scaleToOffset(value))
    private val scrollMutex = MutatorMutex()

    internal val valueAsFraction: Float
        get() {
            return calcFraction(0f, totalWidth, rawOffset)
        }

    internal val disabledRangeAsFractions: ClosedFloatingPointRange<Float>
        get() = coerceRange(disabledRange)

    /**
     * Current value of the slider
     * If value of the slider is out of the [range] it will be coerced into it
     */
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
        handleValueUpdate(userValue, newRawOffset)
    }

    override suspend fun drag(
        dragPriority: MutatePriority,
        block: suspend DragScope.() -> Unit,
    ): Unit = coroutineScope {
        isDragging = true
        scrollMutex.mutateWith(dragScope, dragPriority, block)
        isDragging = false
    }

    internal fun handlePress(offset: Offset) {
        val userValue = scaleToUserValue(offset.x)
        handleValueUpdate(userValue, offset.x)
    }

    internal fun updateDimensions(totalWidth: Float, thumbWidth: Float) {
        this.totalWidth = totalWidth
        this.thumbWidth = thumbWidth
    }

    private fun handleValueUpdate(value: Float, offset: Float) {
        if (onValueChange != null) {
            onValueChange?.invoke(value)
        } else {
            rawOffset = offset
        }
    }

    private fun coerceRange(
        subrange: ClosedFloatingPointRange<Float>,
    ): ClosedFloatingPointRange<Float> {
        if (subrange.isEmpty()) return subrange
        val interpolatedRange = valueDistribution.interpolate(range)
        val interpolatedSubrange = valueDistribution.interpolate(subrange)
        return calcFraction(
            interpolatedRange.start,
            interpolatedRange.endInclusive,
            interpolatedSubrange.start,
        )..calcFraction(
            interpolatedRange.start,
            interpolatedRange.endInclusive,
            interpolatedSubrange.endInclusive,
        )
    }

    private fun scaleToUserValue(offset: Float): Float {
        val range = valueDistribution.interpolate(range)
        val scaledUserValue = scale(0f, totalWidth, offset, range.start, range.endInclusive)
        return valueDistribution.inverse(scaledUserValue)
            .coerceIn(this.range)
            .coerceIntoDisabledRange()
    }

    private fun Float.coerceIntoDisabledRange(): Float {
        if (disabledRange.isEmpty()) return this
        // check if disabled range is on the left or right
        return if (disabledRange.start == range.start) {
            coerceAtLeast(disabledRange.endInclusive)
        } else {
            coerceAtMost(disabledRange.start)
        }
    }

    private fun scaleToOffset(value: Float): Float {
        val coerced = value.coerceIn(range).coerceIntoDisabledRange()
        val interpolatedRange = valueDistribution.interpolate(range)
        val interpolated = valueDistribution.interpolate(coerced)
        return scale(
            interpolatedRange.start,
            interpolatedRange.endInclusive,
            interpolated,
            0f,
            totalWidth,
        )
    }
}

@Composable
public fun rememberSliderState(
    value: Float,
    valueDistribution: SliderValueDistribution = SliderValueDistribution.Linear,
    disabledRange: ClosedFloatingPointRange<Float> = EmptyRange,
): SliderState {
    return remember { SliderState(value, disabledRange, valueDistribution) }
}
