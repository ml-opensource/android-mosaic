package io.monstarlab.mosaic.slider.distribution

internal fun SliderValuesDistribution.inverse(
    range: ClosedFloatingPointRange<Float>,
): ClosedFloatingPointRange<Float> {
    if (range.isEmpty()) return range
    println("inverse ${range.start } ${range.endInclusive}")
    return inverse(range.start)..inverse(range.endInclusive)
}

internal fun SliderValuesDistribution.interpolate(
    range: ClosedFloatingPointRange<Float>,
): ClosedFloatingPointRange<Float> {
    if (range.isEmpty()) return range
    return interpolate(range.start)..interpolate(range.endInclusive)
}
