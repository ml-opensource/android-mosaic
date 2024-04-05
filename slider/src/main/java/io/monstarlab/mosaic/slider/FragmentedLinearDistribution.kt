package io.monstarlab.mosaic.slider

public class FragmentedLinearDistribution private constructor(
    equationMap: Map<Float, LinearEquation>,
) : SliderValueDistribution {
    private var _equationList: MutableList<RangedLinearEquation> = mutableListOf()
    public val equationList: List<RangedLinearEquation> get() = _equationList.toList()

    private lateinit var valueRange: ClosedFloatingPointRange<Float>

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
        valueRange = valueFromOffset(0f)..valueFromOffset(1f)
    }


    override fun interpolate(value: Float): Float =
        valueFromOffset(value).valueToFraction(valueRange)

    override fun inverse(value: Float): Float = offsetFromValue(
        value.fractionToValue(valueRange)
    )

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

    private fun valueFromOffset(offset: Float): Float =
        _equationList.firstOrNull { offset in it.offsetRange }?.equation?.valueFromOffset(offset)
            ?: 0f

    private fun offsetFromValue(value: Float): Float =
        _equationList.firstOrNull { value in it.valueRange }?.equation?.offsetFromValue(value) ?: 0f


    public class Builder {
        private var equationRangeStartMap = mutableMapOf<Float, LinearEquation>()
        private var maxRangeValue: Float = -1f
        private var lastEquation: LinearEquation? = null
        private var rangeStart: Float = 0f
        private var rangeEnd: Float = 1f


        /**
         * Create a distribution on a range with an initial sensitivity provided by [initialSensitivity]
         * please note that the range is for the offset and not the value we get from the offset
         *
         * @param offsetRangeStart the start of the range that the offset values can start from
         * @param offsetRangeEnd the end of the range  that the offset values can go up to
         */
        public fun createDistribution(
            offsetRangeStart: Float,
            offsetRangeEnd: Float,
            initialSensitivity: Float
        ) {
            rangeStart = offsetRangeStart
            rangeEnd = offsetRangeEnd
            sliceAt(initialSensitivity, fractionalValue(rangeStart))
        }


        private fun fractionalValue(value: Float) = value.valueToFraction(rangeStart, rangeEnd)

        public fun sliceAt(
            sensitivity: Float,
            position: Float,
        ): Builder = apply {
            val  relativePosition = fractionalValue(position)
            if (equationRangeStartMap.isEmpty() && relativePosition != 0f) {
                throw FirstValueNotZeroException()
            }
            if (position !in rangeStart..rangeEnd) throw OutOfRangeException()
            if (position < maxRangeValue) throw OverlappingRangeException()

            val equationRangeStartPair =
                calcNextEquation(sensitivity, relativePosition, lastEquation)
            equationRangeStartMap[equationRangeStartPair.first] = equationRangeStartPair.second
            lastEquation = equationRangeStartPair.second
            maxRangeValue = position
        }

        public fun build(): FragmentedLinearDistribution {
            if (equationRangeStartMap.isEmpty()) {
                sliceAt(1f, 0f)
            }
            return FragmentedLinearDistribution(equationRangeStartMap)
        }

        private fun calcNextEquation(
            slope: Float,
            startPos: Float,
            previousEquation: LinearEquation?,
        ): Pair<Float, LinearEquation> {
            val y0 = previousEquation?.valueFromOffset(startPos) ?: 0f
            val c = y0 - slope * startPos
            return Pair(startPos, LinearEquation(slope, c))
        }
    }

    public class FirstValueNotZeroException :
        Exception("The range of the first value added should start from zero")

    public class OutOfRangeException : Exception("The range value should be between 0 and 1")

    public class OverlappingRangeException :
        Exception("can't add overlapping ranges with different sensitivity")
}
