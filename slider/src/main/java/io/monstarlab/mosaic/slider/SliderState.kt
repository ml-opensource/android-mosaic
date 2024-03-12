package io.monstarlab.mosaic.slider

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.gestures.DragScope
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

public class SliderState(
    private val valueDistribution: SliderValueDistribution,
) : DraggableState {

    private var isDragging by mutableStateOf(false)
    private var rawOffset by mutableStateOf(0f)
    private val scrollMutex = MutatorMutex()
    private var totalWidth = 0f
    private var thumbWidth = 0f

    internal var onValueChange: (Float) -> Unit = {}
    internal var range: ClosedFloatingPointRange<Float> = 0f..1f

    public var value: Float
        set(value) {
            rawOffset = scaleToOffset(value)
        }
        get() {
            return valueAsFraction
        }

    internal val valueAsFraction: Float get() {
        return calcFraction(0f, totalWidth, rawOffset)
    }

    private val dragScope: DragScope = object : DragScope {
        override fun dragBy(pixels: Float): Unit = dispatchRawDelta(pixels)
    }

    override fun dispatchRawDelta(delta: Float) {
        rawOffset += delta
        onValueChange(scaleToUserValue(rawOffset))
    }

    override suspend fun drag(dragPriority: MutatePriority, block: suspend DragScope.() -> Unit) {
        isDragging = true
        scrollMutex.mutateWith(dragScope, dragPriority, block)
        isDragging = false
    }

    internal fun updateDimensions(totalWidth: Float, thumbWidth: Float) {
        this.totalWidth = totalWidth
        this.thumbWidth = thumbWidth
    }

    private fun scaleToUserValue(value: Float): Float {
        return scale(0f, totalWidth, value, range.start, range.endInclusive)
    }

    private fun scaleToOffset(value: Float): Float {
        return scale(range.start, range.endInclusive, value, 0f, totalWidth)
    }
}

@Composable
public fun rememberSliderState(): SliderState {
    return remember { SliderState(SliderValueDistribution.Linear) }
}
