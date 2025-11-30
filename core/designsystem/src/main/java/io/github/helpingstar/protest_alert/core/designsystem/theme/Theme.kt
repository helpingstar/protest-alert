package io.github.helpingstar.protest_alert.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun PaTheme(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider() {
        MaterialTheme(
            typography = PaTypography,
            content = content
        )
    }
}