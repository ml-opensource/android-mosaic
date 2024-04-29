package io.monstarlab.mosaic.slider.math

internal data class RangedLinearEquation(
    val equation: LinearEquation,
    val offsetRange: ClosedFloatingPointRange<Float>,
    val valueRange: ClosedFloatingPointRange<Float>,
)
