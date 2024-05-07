package io.monstarlab.mosaic

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSliderClick: () -> Unit,
    onCarouselClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Scaffold(
    modifier = modifier,
    topBar = { TopAppBar(title = { Text(text = "Mosaic Demo") }) },
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_mosaic_log),
            contentDescription = "logo",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterHorizontally),
        )

        Text(
            text = "Collection of Jetpack Compose UI components and utilities.",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
        )

        Column {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier,
            ) {
                DemoButton(
                    resource = R.drawable.ic_slider,
                    text = "Slider",
                    modifier = Modifier.weight(0.3f),
                    onClick = onSliderClick,
                )

                DemoButton(
                    resource = R.drawable.ic_carousel,
                    text = "Carousel",
                    modifier = Modifier.weight(0.3f),
                    onClick = onCarouselClick,
                )

                Spacer(modifier = Modifier.weight(0.3f))
            }
        }
    }
}

@Composable
private fun DemoButton(
    resource: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 16.dp,
        tonalElevation = 2.dp,
        onClick = onClick,
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Spacer(modifier = Modifier)

            Icon(
                painter = painterResource(id = resource),
                contentDescription = text,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.CenterHorizontally),
            )

            Text(
                text = text,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewHomeScreen() {
    HomeScreen({}, {})
}
