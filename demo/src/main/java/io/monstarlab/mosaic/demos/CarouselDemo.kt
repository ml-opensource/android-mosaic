package io.monstarlab.mosaic.demos

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.monstarlab.mosaic.carousel.Carousel
import io.monstarlab.mosaic.carousel.CarouselProgressIndicator
import io.monstarlab.mosaic.carousel.rememberCarouselState
import io.monstarlab.mosaic.ui.theme.MosaicTheme
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarouselDemo() = Scaffold(
    topBar = {
        TopAppBar(title = { Text(text = "Mosaic Carousel")   })
    }
) {
    val state = rememberCarouselState(
        itemsCount = 3,
        stayDuration = 5.seconds
    )

    Box(modifier = Modifier.padding(it)) {

        Carousel(
            state = state,
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { it }
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { -it }
                )
            },

        ) {
            val icon = when(it) {
                0 -> Icons.Rounded.Settings
                1 -> Icons.Rounded.Favorite
                else -> Icons.Rounded.Email
            }

            val text = when(it) {
                0 -> "Est deserunt cillum ipsum aute reprehenderit labore Lorem enim tempor enim incididunt quis dolore anim fugiat."
                1 -> "Nulla commodo voluptate aliquip. Exercitation laboris laborum laborum laborum."
                else -> "Officia ea voluptate nostrud quis. Adipisicing Officia ea voluptate nostrud quis. Adipisicing."
            }

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

        }

        CarouselProgressIndicator(
            state = state,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .height(2.dp)
        )
    }
}

@Preview
@Composable
private fun PreviewCarouselDemo() {
    MosaicTheme {
        CarouselDemo()
    }
}