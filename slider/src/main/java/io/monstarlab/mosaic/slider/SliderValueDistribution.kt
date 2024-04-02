package io.monstarlab.mosaic.slider

import androidx.annotation.FloatRange
import kotlin.math.sqrt

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
        public fun parabolic(
            a: Float,
            b: Float = 0f,
            c: Float = 0f,
        ): SliderValueDistribution {
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

internal fun SliderValueDistribution.interpolate(
    range: ClosedFloatingPointRange<Float>,
): ClosedFloatingPointRange<Float> {
    return interpolate(range.start)..interpolate(range.endInclusive)
}

internal fun SliderValueDistribution.inverse(
    range: ClosedFloatingPointRange<Float>,
): ClosedFloatingPointRange<Float> {
    return inverse(range.start)..inverse(range.endInclusive)
}

/**
 * Represents a parabolic distribution strategy for slider values.
 *
 * This strategy calculates interpolated and inversely interpolated values based on a parabolic equation:
 * f(x) = a * x^2 + b * x + c
 *
 * @property a coefficient of the x^2 term in the parabolic equation
 * @property b coefficient of the x term in the parabolic equation
 * @property c constant term in the parabolic equation
 * @constructor Creates a ParabolicValueDistribution with the given coefficients.
 */
public class ParabolicValueDistribution(
    private val a: Float,
    private val b: Float,
    private val c: Float,
) : SliderValueDistribution {

    override fun inverse(value: Float): Float {
        if (value == 0f) return 0f

        val d = (b * b) - 4 * a * (c - value)
        return (-b + sqrt(d)) / (2 * a)
    }

    override fun interpolate(value: Float): Float {
        return a * (value * value) + b * value + c
    }
}
