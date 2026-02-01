package io.github.helpingstar.protest_alert.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color


val LightDefaultColorScheme = lightColorScheme(
    surfaceVariant = Color(0xFFF9FAFB),
    primary = Color(0xFF2B7FFF),
    surfaceContainer = Color(0xFFF3F4F6),
    onSurfaceVariant = Color(0xFF6A7282),
    onSurface = Color(0xFF101828),
    outline = Color(0xFFF3F4F6),
    surface = Color(0xFFFFFFFF),
    background = Color(0xFFF9FAFB),
    onBackground = Color(0xFF1E2939),
)

@Composable
fun PaTheme(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider() {
        MaterialTheme(
            colorScheme = LightDefaultColorScheme,
            typography = PaTypography,
            content = content
        )
    }
}