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
import io.monstarlab.mosaic.slider.distribution.SliderValueDistribution
import kotlinx.coroutines.coroutineScope

/**
 * A compose state for the Slider component
 * Responsible for managing internal properties such as offset value and drag / click behaviours
 */
public class SliderState(
    initialValue: Float,
    public val range: ClosedFloatingPointRange<Float>,
    private val disabledRange: ClosedFloatingPointRange<Float>,
    private val valueDistribution: SliderValueDistribution,
) : DraggableState {

    /**
     * Optional callback for notifying the change of value
     * In case provided, the "value" state is stored outside in another component
     * And it is the responsibility of a client to manage the value state
     */
    internal var onValueChange: ((Float) -> Unit)? = null
    internal var isDragging by mutableStateOf(false)
        private set
    private var totalWidth by mutableFloatStateOf(0f)
    private var thumbWidth by mutableFloatStateOf(0f)

    /**
     * Internal state to hold the User Value (coerced and in range)
     * Serves as source of truth for the client
     */
    private var valueState by mutableFloatStateOf(initialValue)

    /**
     * Internally stored offset, used simply for the drag & click gestures
     */
    private var rawOffset by mutableFloatStateOf(scaleToOffset(initialValue))
    private val scrollMutex = MutatorMutex()

    /**
     * Current value of the slider
     * If value of the slider is out of the [range] it will be coerced into it
     * If the value of the slider is inside the [disabledRange] It will be coerced int closes available range that is not disabled
     *
     */
    public var value: Float
        get() = valueState
        set(value) {
            valueState = coerceUserValue(value)
        }

    /**
     * Internal value returned as fraction, used for displaying and specifying
     * the "real" position of the thumb
     */
    internal val offsetAsFraction: Float
        get() = if (totalWidth == 0f) 0f else {
            val valueFraction = value.valueToFraction(range)
            valueDistribution.inverse(valueFraction).coerceIn(0f,1f)
        }


    internal val disabledRangeAsFractions: ClosedFloatingPointRange<Float>
        get() = coerceRangeIntoFractions(disabledRange)

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
        rawOffset = offset
        if (onValueChange != null) {
            onValueChange?.invoke(value)
        } else {
            valueState = value
        }
    }

    /**
     * Scales offset in to the value that user should see
     */
    private fun scaleToUserValue(offset: Float): Float {
        val coercedValue = (offset / totalWidth).coerceIn(0f..1f)
        val value = valueDistribution.interpolate(coercedValue)
            .fractionToValue(range)
        return coerceUserValue(value)
    }

    /**
     * Converts value of the user into the raw offset on the track
     */
    private fun scaleToOffset(value: Float): Float {
        val valueAsFraction = coerceUserValue(value).valueToFraction(range)
        return valueDistribution.inverse(valueAsFraction).fractionToValue(0f, totalWidth)
    }

    internal fun coerceUserValue(value: Float): Float {
        return value
            .coerceIn(range)
            .coerceIntoDisabledRange()
    }

    private fun Float.coerceIntoDisabledRange(): Float {
        if (disabledRange.isEmpty() || !disabledRange.contains(this)) return this
        if (this - disabledRange.start < disabledRange.endInclusive - this) disabledRange.start else disabledRange.endInclusive
        // check if disabled range is on the left or right

        return if (disabledRange.start == range.start) {
            coerceAtLeast(disabledRange.endInclusive)
        } else {
            coerceAtMost(disabledRange.start)
        }
    }

    private fun coerceRangeIntoFractions(
        subrange: ClosedFloatingPointRange<Float>,
    ): ClosedFloatingPointRange<Float> {
        if (subrange.isEmpty()) return subrange
        val start = valueDistribution.inverse(subrange.start.valueToFraction(range))
        val end = valueDistribution.inverse(subrange.endInclusive.valueToFraction(range))
        return start..end
    }
}

@Composable
public fun rememberSliderState(
    value: Float,
    range: ClosedFloatingPointRange<Float> = 0f..1f,
    valueDistribution: SliderValueDistribution = SliderValueDistribution.Linear,
    disabledRange: ClosedFloatingPointRange<Float> = EmptyRange,
): SliderState {
    return remember(range, valueDistribution, disabledRange) {
        SliderState(
            initialValue = value,
            range = range,
            disabledRange = disabledRange,
            valueDistribution = valueDistribution,
        )
    }
}
