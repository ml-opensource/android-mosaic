package io.monstarlab.mosaic.slider

public interface SliderValueDistribution {

    public fun interpolate(value: Float) : Float

    public fun inverse(value: Float) : Float


    public companion object {
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