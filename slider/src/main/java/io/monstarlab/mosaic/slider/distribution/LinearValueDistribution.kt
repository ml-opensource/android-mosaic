package io.monstarlab.mosaic.slider.distribution

/**
 * Represents a linear distribution strategy for slider values.
 */
public object LinearValueDistribution : SliderValueDistribution {
    override fun interpolate(value: Float): Float {
        return value
    }

    override fun inverse(value: Float): Float {
        return value
    }
}
