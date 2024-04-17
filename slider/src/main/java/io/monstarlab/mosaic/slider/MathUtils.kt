package io.monstarlab.mosaic.slider

import androidx.compose.ui.util.lerp

internal fun scale(a1: Float, b1: Float, x1: Float, a2: Float, b2: Float) =
    lerp(a2, b2, calcFraction(a1, b1, x1))

internal fun calcFraction(a: Float, b: Float, pos: Float) =
    (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)

internal fun Float.valueToFraction(rangeStart: Float, rangeEnd: Float) =
    calcFraction(rangeStart, rangeEnd, this)

internal fun Float.valueToFraction(range: ClosedFloatingPointRange<Float>) =
    valueToFraction(range.start, range.endInclusive)

internal fun Float.fractionToValue(rangeStart: Float, rangeEnd: Float): Float =
    scale(0f, 1f, this.coerceIn(0f, 1f), rangeStart, rangeEnd)

internal fun Float.fractionToValue(range: ClosedFloatingPointRange<Float>): Float =
    scale(0f, 1f, this, range.start, range.endInclusive)