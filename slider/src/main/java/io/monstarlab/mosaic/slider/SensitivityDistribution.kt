package io.monstarlab.mosaic.slider

public class SensitivityDistribution private constructor(
    equationMap: Map<Float, LinearEquation>,
) {
    private var _equationList: MutableList<RangedLinearEquation> =
        mutableListOf<RangedLinearEquation>()
    public val equationList: List<RangedLinearEquation> get() = _equationList.toList()

    init {
        var previous: Pair<Float, LinearEquation>? = null
        // get the max range start value to help us identify the last equation
        val maxRangeStart = equationMap.maxOf { it.key }
        equationMap.forEach { pair ->
            previous?.let {
                addEquation(it.second, it.first, pair.key)
            }
            // add the last equation in the range and make sure the range for it ends at 1f
            if (pair.key == maxRangeStart) {
                addEquation(pair.value, pair.key, 1f)
            }
            previous = pair.toPair()
        }
    }

    private fun addEquation(equation: LinearEquation, rangeStart: Float, rangeEnd: Float) {
        val offsetRange = rangeStart..rangeEnd
        val valueRange =
            equation.valueFromOffset(rangeStart)..equation.valueFromOffset(rangeEnd)
        _equationList.add(
            RangedLinearEquation(
                equation = equation,
                offsetRange = offsetRange,
                valueRange = valueRange,
            ),
        )
    }

    public fun valueFromOffset(offset: Float): Float? =
        _equationList.firstOrNull { offset in it.offsetRange }?.equation?.valueFromOffset(offset)

    public fun offsetFromValue(value: Float): Float? =
        _equationList.firstOrNull { value in it.valueRange }?.equation?.offsetFromValue(value)

    public class Builder {
        private var equationRangeStartMap = mutableMapOf<Float, LinearEquation>()
        private var maxRangeValue: Float = -1f
        private var lastEquation: LinearEquation? = null

        public fun add(sensitivity: Float, rangeStart: Float): Builder = apply {
            if (equationRangeStartMap.isEmpty() && rangeStart != 0f) {
                throw FirstValueNotZeroException()
            }
            if (rangeStart !in 0.0f..1f) throw OutOfRangeException()
            if (rangeStart < maxRangeValue) throw OverlappingRangeException()

            val equationRangeStartPair = calcNextEquation(sensitivity, rangeStart, lastEquation)
            equationRangeStartMap[equationRangeStartPair.first] = equationRangeStartPair.second
            lastEquation = equationRangeStartPair.second
            maxRangeValue = rangeStart
        }

        private fun calcNextEquation(
            slope: Float,
            rangeStart: Float,
            previousEquation: LinearEquation?,
        ): Pair<Float, LinearEquation> {
            val y0 = previousEquation?.valueFromOffset(rangeStart) ?: 0f
            val c = y0 - slope * rangeStart
            return Pair(rangeStart, LinearEquation(slope, c))
        }

        public fun build(): SensitivityDistribution {
            return SensitivityDistribution(equationRangeStartMap)
        }
    }

    public class FirstValueNotZeroException :
        Exception("The range of the first value added should start from zero")

    public class OutOfRangeException : Exception("The range value should be between 0 and 1")

    public class OverlappingRangeException :
        Exception("can't add overlapping ranges with different sensitivity")
}
