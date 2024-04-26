package io.monstarlab.mosaic.slider.distribution

import io.monstarlab.mosaic.slider.valueToFraction

public class ValueCheckPointDistribution(valuesMap: List<Pair<Float, Float>>) :
    SliderValueDistribution {

    private var equations: List<RangedLinearEquation>

    init {
        require(valuesMap.isNotEmpty()) { "values map can't be empty" }
        val offsetRange = valuesMap.minOf { it.first }..valuesMap.maxOf { it.first }
        val valueRange = valuesMap.minOf { it.second }..valuesMap.maxOf { it.second }
        val zipped = valuesMap.sortedBy { it.first }
            .zipWithNext()
        // make sure all values are increasing, otherwise throw an exception
        zipped.firstOrNull { it.first.second >= it.second.second }
            ?.let {
                throw DecreasingValueException(it.first)
            }
        equations = zipped.map {
            val x1Fraction = it.first.first.valueToFraction(offsetRange)
            val x2Fraction = it.second.first.valueToFraction(offsetRange)
            val y1Fraction = it.first.second.valueToFraction(valueRange)
            val y2Fraction = it.second.second.valueToFraction(valueRange)
            val equation = LinearEquation.fromTowPoints(
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


    override fun interpolate(value: Float): Float =
        equations.first { it.offsetRange.contains(value) }.equation.valueFromOffset(value)


    override fun inverse(value: Float): Float =
        equations.first { it.valueRange.contains(value) }.equation.offsetFromValue(value)


    public class DecreasingValueException(progressValuePair: Pair<Float, Float>) :
        Exception(
            "Values must be always increasing with increasing progress," +
                    " item at progress ${progressValuePair.first}  with value " +
                    "${progressValuePair.second} is breaking this rule "
        )
}