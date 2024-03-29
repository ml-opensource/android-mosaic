package io.monstarlab.mosaic.slider

public data class RangedLinearEquation(
    val equation: LinearEquation,
    val offsetRange: ClosedFloatingPointRange<Float>,
    val valueRange: ClosedFloatingPointRange<Float>,
)
