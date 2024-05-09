package io.monstarlab.mosaic.carousel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlin.time.Duration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Represents the state of a carousel.
 * @param itemsCount The total number of items in the carousel.
 * @param stayDuration The duration each item should stay on screen.
 * @param scope The coroutine scope to use for the carousel.
 */
public class MosaicCarouselState(
    public val itemsCount: Int,
    private val stayDuration: Duration,
    private val scope: CoroutineScope,
) {

    private var carouselProgress: Float by mutableFloatStateOf(0f)

    private val itemProgressIncrement: Float by lazy {
        val refreshRate = CarouselDefaults.refreshRate.inWholeMilliseconds.toFloat()
        val stayDuration = stayDuration.inWholeMilliseconds.toFloat()
        refreshRate / stayDuration
    }

    private var job: Job? = null

    /**
     * The current item index.
     */
    public val currentItem: Int
        get() {
            return carouselProgress.toInt()
        }

    /**
     * The progress of the current item.
     * This value is between 0 and 1.
     */
    public val currentItemProgress: Float
        get() {
            return (carouselProgress - currentItem).coerceIn(0f, 1f)
        }

    /**
     * Stops the carousel and any ongoing animations.
     */
    public fun stop() {
        job?.cancel()
    }

    /**
     * Moves to the previous item and setting its progress to 0.
     */
    public fun moveToPrevious() {
        updateProgress(currentItem - 1f)
    }

    /**
     * Moves to the next item and setting its progress to 0.
     */
    public fun moveToNext() {
        updateProgress(currentItem + 1f)
    }

    /**
     * Moves to the specified item index.
     * @param index The index of the item to move to.
     */
    public fun moveTo(index: Int) {
        updateProgress(index.toFloat())
    }

    /**
     * Starts the carousel.
     * The carousel will automatically move to the next item at the specified interval.
     * The interval is determined by the [stayDuration] property.
     */
    public fun start() {
        job?.cancel()
        job = scope.launch {
            while (true) {
                delay(CarouselDefaults.refreshRate)
                updateProgress(carouselProgress + itemProgressIncrement)
                println(currentItemProgress)
            }
        }
    }

    private fun updateProgress(progress: Float) {
        carouselProgress = when {
            progress < 0 -> 0f
            progress > itemsCount -> 0f
            else -> progress
        }
    }
}

/**
 * Creates a [MosaicCarouselState] that will remember its state across compositions.
 * @param itemsCount The total number of items in the carousel.
 * @param stayDuration The duration each item should stay on screen. By default, it is 2 seconds.
 * @return A [MosaicCarouselState] that will remember its state across compositions.
 */
@Composable
public fun rememberCarouselState(
    itemsCount: Int,
    stayDuration: Duration = CarouselDefaults.itemDuration,
): MosaicCarouselState {
    val scope = rememberCoroutineScope()

    return remember(itemsCount) {
        MosaicCarouselState(
            itemsCount = itemsCount,
            stayDuration = stayDuration,
            scope = scope,
        )
    }
}
