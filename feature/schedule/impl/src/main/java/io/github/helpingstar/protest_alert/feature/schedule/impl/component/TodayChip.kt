package io.github.helpingstar.protest_alert.feature.schedule.impl.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaTheme
import io.github.helpingstar.protest_alert.core.designsystem.theme.fontFamily

/**
 * Today chip component
 *
 * Figma element name: Container
 * Figma element type: Frame
 * Figma node-id: 194:1047
 *
 * Displays a red pill-shaped chip with "오늘" (Today) text.
 * Used to indicate today's date in the schedule list.
 *
 * Dependencies: None (leaf component)
 *
 * @param modifier Optional modifier for the component
 */
@Composable
fun TodayChip(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = Color(0xFFE11D48), // Figma: #e11d48 (rose-600)
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "오늘",
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TodayChipPreview() {
    PaTheme {
        TodayChip()
    }
}
