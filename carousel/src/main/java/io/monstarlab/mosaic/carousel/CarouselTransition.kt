package io.monstarlab.mosaic.carousel

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith


public typealias CarouselTransition = AnimatedContentTransitionScope<Int>.() -> ContentTransform

internal fun defaultCarouselTransition(): CarouselTransition {
    return {
        fadeIn() togetherWith fadeOut()
    }
}