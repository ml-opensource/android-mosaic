package io.monstarlab.mosaic.slider.distribution

import androidx.annotation.FloatRange

/**
 * Determines how the values will be distributed across the slider
 * Usually the values are distributed in a linear fashion, this interfaces allows
 * to control the distribution using simple math expressions
 * @see ParabolicValuesDistribution to see how values can be distributed using parabolic curve
 */
public interface SliderValuesDistribution {

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
         * Creates a [SliderValuesDistribution] with a parabolic distribution strategy.
         *
         * @param a coefficient of the x^2 term in the parabolic equation
         * @param b coefficient of the x term in the parabolic equation
         * @param c constant term in the parabolic equation
         * @return a [SliderValuesDistribution] instance with a parabolic distribution strategy
         */
        public fun parabolic(a: Float, b: Float = 0f, c: Float = 0f): SliderValuesDistribution {
            return ParabolicValuesDistribution(a, b, c)
        }

        /**
         * Creates a [SliderValuesDistribution] with a distribution strategy based on a list of check points.
         * Each check point is a pair of offset fraction and value that will be associated with with this progress
         * The distribution will interpolate between the check points using linear equations.
         *
         * Example:
         * For the value range of 0..100
         * CheckPointsValueDistribution(listOf(0f to 0f, 0.5f to 80f, 1f to 100f))
         * This will create a distribution that will place value of  80 at 0.5 progress allowing the user to
         * have more precision while selecting values between 80 and 100
         *
         * @param values a list of check points, each check point is a pair of offset fraction and value
         * @return a [SliderValuesDistribution] instance with a check points distribution strategy
         */
        public fun checkpoints(vararg values: Pair<Float, Float>): SliderValuesDistribution {
            return CheckPointsValuesDistribution(values.toList())
        }

        /**
         * A linear distribution strategy where the input value is directly mapped to the output value.
         * Used in [Slider] by default
         */
        public val Linear: SliderValuesDistribution = LinearValuesDistribution
    }
}
