package io.monstarlab.mosaic.carousel

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith

/**
 * A type alias for a transition that can be applied to the carousel content.
 */
public typealias CarouselTransition = AnimatedContentTransitionScope<Int>.() -> ContentTransform

internal fun defaultCarouselTransition(): CarouselTransition {
    return {
        fadeIn() togetherWith fadeOut()
    }
}