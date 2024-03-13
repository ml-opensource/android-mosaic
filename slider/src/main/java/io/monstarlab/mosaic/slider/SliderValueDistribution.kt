package io.monstarlab.mosaic.slider

import kotlin.math.sqrt

public interface SliderValueDistribution {

    public fun interpolate(value: Float): Float

    public fun inverse(value: Float): Float

    public companion object {

        public fun parabolic(a: Float, b: Float, c: Float): SliderValueDistribution {
            return ParabolicValueDistribution(a, b, c)
        }

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

public class ParabolicValueDistribution(
    private val a: Float,
    private val b: Float,
    private val c: Float
) : SliderValueDistribution {

    override fun inverse(value: Float): Float {
        return a * (value * value) + b * value + c
    }

    override fun interpolate(value: Float): Float {
        if (value == 0f) return 0f

        val d = (b * b) - 4 * a * (c - value)
        return (-b + sqrt(d)) / (2 * a)
    }
}
