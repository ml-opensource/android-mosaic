package io.monstarlab.mosaic.slider.distribution

import androidx.annotation.FloatRange

/**
 * Determines how the values will be distributed across the slider
 * Usually the values are distributed in a linear fashion, this interfaces allows
 * to control the distribution using simple math expressions
 * @see ParabolicValueDistribution to see how values can be distributed using parabolic curve
 */
public interface SliderValueDistribution {

    /**
     * Interpolates a value based on the distribution strategy.
     * @param value the normalized input value to interpolate, it must be between 0 and 1
     * @return the normalized interpolated value based on the distribution strategy, between 0 and 1
     */
    public fun interpolate(@FloatRange(0.0, 1.0) value: Float): Float

    /**
     * Inversely interpolates a value from the output range to the input range based on the distribution strategy.
     *
     * @param value the normalized value to inverse interpolate, must be between 0 and 1
     * @return the normalized inverse interpolated value based on the distribution strategy, between 0 and 1
     */
    public fun inverse(@FloatRange(0.0, 1.0) value: Float): Float

    public companion object {

        /**
         * Creates a [SliderValueDistribution] with a parabolic distribution strategy.
         *
         * @param a coefficient of the x^2 term in the parabolic equation
         * @param b coefficient of the x term in the parabolic equation
         * @param c constant term in the parabolic equation
         * @return a [SliderValueDistribution] instance with a parabolic distribution strategy
         */
        public fun parabolic(a: Float, b: Float = 0f, c: Float = 0f): SliderValueDistribution {
            return ParabolicValueDistribution(a, b, c)
        }

        /**
         * A linear distribution strategy where the input value is directly mapped to the output value.
         * Used in [Slider] by default
         */
        public val Linear: SliderValueDistribution = object : SliderValueDistribution {
            override fun interpolate(value: Float): Float {
                return value
            }

            override fun inverse(value: Float): Float {
                return value
            }
        }
    }
}
