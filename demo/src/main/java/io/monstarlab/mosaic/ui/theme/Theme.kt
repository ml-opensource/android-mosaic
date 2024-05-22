package io.monstarlab.mosaic.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MonstarlabYellow,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = DarkGrey,
    surface = Color.Black,
)

@Composable
fun MosaicTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content,
    )
}
