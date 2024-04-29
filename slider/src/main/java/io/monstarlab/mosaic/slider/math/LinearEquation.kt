package io.monstarlab.mosaic.slider.math

internal data class LinearEquation(
    private val m: Float,
    private val c: Float,
) {
    internal fun valueFromOffset(offset: Float): Float = m * offset + c

    internal fun offsetFromValue(value: Float): Float = (value - c) / m

    internal companion object {
        internal fun fromTwoPoints(x1: Float, y1: Float, x2: Float, y2: Float): LinearEquation {
            require(x2 != x1) { "can't calc equation from points with similar x value" }
            val slope = (y2 - y1) / (x2 - x1)
            val c = y2 - slope * x2
            return LinearEquation(slope, c)
        }
    }
}
