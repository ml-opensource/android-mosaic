package io.monstarlab.mosaic.slider.distribution

import io.monstarlab.mosaic.slider.math.LinearEquation
import io.monstarlab.mosaic.slider.math.Point
import io.monstarlab.mosaic.slider.math.RangedLinearEquation
import io.monstarlab.mosaic.slider.math.valueToFraction

/**
 * Represents a distribution strategy for slider values based on a list of check points.
 * Each check point is a pair of offset fraction and value that will be associated with with this progress
 * The distribution will interpolate between the check points using linear equations.
 *
 * Example:
 * For the value range of 0..100
 * CheckPointsValueDistribution(listOf(0f to 0f, 0.5f to 80f, 1f to 100f))
 * This will create a distribution that will place value of  80 at 0.5 progress allowing the user to
 * have more precision while selecting values between 80 and 100
 */
public class CheckPointsValuesDistribution(
    valuesMap: List<Pair<Float, Float>>,
) :
    SliderValuesDistribution {

    private var equations: List<RangedLinearEquation>

    init {
        require(valuesMap.isNotEmpty()) {
            "Values map can't be empty"
        }

        val offsetRange = valuesMap.minOf { it.first }..valuesMap.maxOf { it.first }
        val valueRange = valuesMap.minOf { it.second }..valuesMap.maxOf { it.second }

        equations = valuesMap.sortedBy { it.first }
            .zipWithNext()
            .checkIncreasingValues() // check if values are always increasing
            .map {
                val x1Fraction = it.first.first.valueToFraction(offsetRange)
                val x2Fraction = it.second.first.valueToFraction(offsetRange)
                val y1Fraction = it.first.second.valueToFraction(valueRange)
                val y2Fraction = it.second.second.valueToFraction(valueRange)
                val equation = LinearEquation.fromTwoPoints(
                    x1 = x1Fraction,
                    x2 = x2Fraction,
                    y1 = y1Fraction,
                    y2 = y2Fraction,
                )
                RangedLinearEquation(
                    equation = equation,
                    offsetRange = x1Fraction..x2Fraction,
                    valueRange = y1Fraction..y2Fraction,
                )
            }
    }

    override fun interpolate(value: Float): Float {
        val equation = equations.firstOrNull { it.offsetRange.contains(value) }?.equation
        checkNotNull(equation) { "No equation found for value $value during interpolate" }
        return equation.valueFromOffset(value)
    }

    override fun inverse(value: Float): Float {
        val equation = equations.firstOrNull { it.valueRange.contains(value) }?.equation
        checkNotNull(equation) { "No equation found for value $value during inverse" }
        return equation.offsetFromValue(value)
    }

    private fun List<Pair<Point, Point>>.checkIncreasingValues(): List<Pair<Point, Point>> {
        find { it.first.second >= it.second.second }?.let {
            throw DecreasingValueException(it.first)
        }
        return this
    }

    public class DecreasingValueException(progressValuePair: Point) :
        IllegalStateException(
            "Values must be always increasing with increasing progress," +
                " item at progress ${progressValuePair.first}  with value " +
                "${progressValuePair.second} is breaking this rule ",
        )
}
