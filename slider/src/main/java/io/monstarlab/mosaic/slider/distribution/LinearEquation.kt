package io.monstarlab.mosaic.slider.distribution

public data class LinearEquation(
    private val m: Float,
    private val c: Float,
) {
    public fun valueFromOffset(offset: Float): Float = m * offset + c

    public fun offsetFromValue(value: Float): Float = (value - c) / m

   public companion object{
       public fun fromTowPoints(x1:Float,y1:Float,x2:Float,y2:Float): LinearEquation {
           require(x2!=x1){"can't calc equation from points with similar x value"}
           val slope = (y2-y1)/(x2-x1)
           val c = y2 - slope*x2
           return  LinearEquation(slope,c)
       }
   }
}
