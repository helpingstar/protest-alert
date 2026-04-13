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
 * Date chip variation.
 */
enum class DateChipVariant {
    TODAY,
    TOMORROW
}

/**
 * Date chip component
 *
 * Figma element name: Container
 * Figma element type: Frame
 *
 * Displays a pill-shaped chip indicating relative dates in the schedule list.
 *
 * Dependencies: None (leaf component)
 *
 * @param variant The date chip variation to display
 * @param modifier Optional modifier for the component
 */
@Composable
fun DateChip(
    variant: DateChipVariant,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, text) = when (variant) {
        DateChipVariant.TODAY -> Triple(
            Color(0xFFE11D48),
            Color.White,
            "오늘"
        )

        DateChipVariant.TOMORROW -> Triple(
            Color(0xFFEBF5FF),
            Color(0xFF2B7FFF),
            "내일"
        )
    }

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DateChipTodayPreview() {
    PaTheme {
        DateChip(variant = DateChipVariant.TODAY)
    }
}

@Preview(showBackground = true)
@Composable
private fun DateChipTomorrowPreview() {
    PaTheme {
        DateChip(variant = DateChipVariant.TOMORROW)
    }
}
