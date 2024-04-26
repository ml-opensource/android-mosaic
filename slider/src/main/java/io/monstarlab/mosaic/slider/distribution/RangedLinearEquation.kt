package io.monstarlab.mosaic.slider.distribution

public data class RangedLinearEquation(
    val equation: LinearEquation,
    val offsetRange: ClosedFloatingPointRange<Float>,
    val valueRange: ClosedFloatingPointRange<Float>,
)
