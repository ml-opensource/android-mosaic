package io.monstarlab.mosaic.carousel

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
public fun CarouselProgressIndicator(
    state: CarouselState,
    modifier: Modifier = Modifier,
) {
    CarouselProgressIndicator(
        itemsTotal = state.itemsCount,
        currentItem = state.currentItem,
        currentItemProgress = state.currentItemProgress,
        modifier = modifier
    )
}

@Composable
internal fun CarouselProgressIndicator(
    itemsTotal: Int,
    currentItem: Int,
    currentItemProgress: Float,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        modifier = modifier
    ) {
        repeat(itemsTotal) { index ->
            val progress = when {
                index < currentItem -> 1f
                index == currentItem -> currentItemProgress
                else -> 0f
            }
            ProgressSegment(
                progress = progress,
                modifier = Modifier.weight(1f / itemsTotal),
                color = Color.White,
            )
        }
    }

}



@Composable
private fun ProgressSegment(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    inactiveColor: Color = color.copy(alpha = 0.5f),
) {
    Canvas(
        modifier = modifier
            .height(3.dp)
            .fillMaxWidth(),
    ) {
        val activeWidth = size.width * progress
        drawRect(
            color = color,
            topLeft = Offset.Zero,
            size = Size(activeWidth, size.height),
        )
        drawRect(
            color = inactiveColor,
            topLeft = Offset(x = activeWidth, y = 0f),
            size = Size(size.width - activeWidth, size.height),
        )
    }
}

@Preview
@Composable
private fun PreviewCarouselProgressIndicator() {
    CarouselProgressIndicator(
        itemsTotal = 3,
        currentItem = 2,
        currentItemProgress = 0.5f,
        modifier = Modifier.fillMaxWidth(),
    )
}