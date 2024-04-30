package io.monstarlab.mosaic.slider.distribution

import kotlin.math.sqrt

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
public class ParabolicValuesDistribution(
    private val a: Float,
    private val b: Float,
    private val c: Float,
) : SliderValuesDistribution {

    override fun inverse(value: Float): Float {
        if (value == 0f) return 0f

        val d = (b * b) - 4 * a * (c - value)
        return (-b + sqrt(d)) / (2 * a)
    }

    override fun interpolate(value: Float): Float {
        return a * (value * value) + b * value + c
    }
}
