package io.monstarlab.mosaic.slider

public data class LinearEquation(
    private val m: Float,
    private val c: Float,
) {
    public fun valueFromOffset(offset: Float): Float = m * offset + c

    public fun offsetFromValue(value: Float): Float = (value - c) / m
}
