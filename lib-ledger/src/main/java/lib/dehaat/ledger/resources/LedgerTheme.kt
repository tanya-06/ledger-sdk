package lib.dehaat.ledger.resources

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = DarkBlue,
    primaryVariant = DarkBlue,
    secondary = IconBlue,
    background = Color.White,
    onBackground = DarkGray,
    surface = Color.White,
    onSurface = DarkGray,
    onPrimary = Color.White,
    onSecondary = Color.White,
)

@Composable
fun LedgerTheme(content: @Composable() () -> Unit) {
    CompositionLocalProvider(LocalSpacing provides Dimensions()) {
        MaterialTheme(
            colors = LightColorPalette,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}