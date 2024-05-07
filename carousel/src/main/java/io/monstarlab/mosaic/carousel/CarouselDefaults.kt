package io.monstarlab.mosaic.carousel

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.milliseconds

internal object CarouselDefaults {
    internal val itemDuration: Duration = 2.seconds
    internal val refreshRate: Duration = 60.milliseconds
}