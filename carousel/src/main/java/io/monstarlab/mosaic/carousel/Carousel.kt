package io.monstarlab.mosaic.carousel

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
public fun Carousel(
    state: CarouselState,
    modifier: Modifier = Modifier,
    label: String = "",
    transitionSpec: CarouselTransition = defaultCarouselTransition(),
    content: @Composable AnimatedContentScope.(Int) -> Unit,
) {
    LaunchedEffect(state) {
        state.start()
    }

    Box(modifier = modifier) {
        AnimatedContent(
            targetState = state.currentItem,
            label = label,
            transitionSpec = transitionSpec,
            content = content,
        )

        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier,
        ) {
            ClickableBox(
                onClick = state::moveToPrevious,
                onHold = state::stop,
                onRelease = state::start,
            )

            ClickableBox(
                onClick = state::moveToNext,
                onHold = state::stop,
                onRelease = state::start,
            )
        }
    }
}

@Composable
private fun RowScope.ClickableBox(
    onClick: () -> Unit = {},
    onHold: () -> Unit = {},
    onRelease: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .weight(0.5f)
            .run {
                if (LocalInspectionMode.current) {
                    border(1.dp, Color.Red)
                } else {
                    this
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = { },
                    onPress = {
                        onHold()
                        awaitRelease()
                        onRelease()
                    },
                )
            },
    )
}

@Preview
@Composable
private fun PreviewCarousel() {
    val state = rememberCarouselState(itemsCount = 5)
    Box(modifier = Modifier.fillMaxSize()) {
        Carousel(state, Modifier.align(Alignment.Center)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .background(if (it % 2 == 0) Color.Yellow else Color.Green),
            ) {
                BasicText(
                    text = "$it",
                    modifier = Modifier.align(Alignment.Center),
                    style = TextStyle.Default.copy(fontSize = 48.sp),
                )
            }
        }
    }
}
