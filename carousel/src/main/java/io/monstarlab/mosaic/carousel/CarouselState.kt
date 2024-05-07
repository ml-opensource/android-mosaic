package io.monstarlab.mosaic.carousel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration

public class CarouselState(
    public val itemsCount: Int,
    private val stayDuration: Duration,
    private val scope: CoroutineScope
) {

    private var carouselProgress: Float by mutableFloatStateOf(0f)

    private val itemProgressIncrement: Float by lazy {
        val refreshRate = CarouselDefaults.refreshRate.inWholeMilliseconds.toFloat()
        val stayDuration = stayDuration.inWholeMilliseconds.toFloat()
        refreshRate / stayDuration
    }

    private var job: Job? = null

    public val currentItem: Int
        get() {
            return carouselProgress.toInt()
        }

    public val currentItemProgress: Float
        get() {
            return (carouselProgress - currentItem).coerceIn(0f, 1f)
        }


    public fun stop() {
        job?.cancel()
    }


    public fun moveToPrevious() {
        updateProgress(currentItem - 1f)
    }

    public fun moveToNext() {
       updateProgress(currentItem + 1f)
    }

    public fun moveTo(index: Int) {
       updateProgress(index.toFloat())
    }

    public fun start()  {
        job?.cancel()
        job =  scope.launch {
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
            progress > itemsCount ->  0f
            else -> progress
        }
    }

}


@Composable
public fun rememberCarouselState(
    itemsCount: Int,
    stayDuration: Duration = CarouselDefaults.itemDuration,
): CarouselState {

    val scope = rememberCoroutineScope()

    return remember(itemsCount) {
        CarouselState(
            itemsCount = itemsCount,
            stayDuration = stayDuration,
            scope = scope
        )
    }
}
