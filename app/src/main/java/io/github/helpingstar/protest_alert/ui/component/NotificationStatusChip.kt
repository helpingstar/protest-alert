package io.github.helpingstar.protest_alert.ui.component

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
 * Notification status type for schedule items
 */
enum class NotificationStatus {
    NORMAL,    // 알림
    EMERGENCY, // 긴급
    PINNED     // 공지
}

/**
 * Notification status chip component
 *
 * Figma element name: NotificationBadge
 * Figma element type: Component
 * Figma node-id: 277:1851
 *
 * Displays a pill-shaped chip indicating the notification status.
 * Has 3 variants: Normal (알림), Emergency (긴급), Pinned (공지).
 *
 * Dependencies: None (leaf component)
 *
 * @param status The notification status to display
 * @param modifier Optional modifier for the component
 */
@Composable
fun NotificationStatusChip(
    status: NotificationStatus,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, text) = when (status) {
        NotificationStatus.NORMAL -> Triple(
            Color(0xFFF3F4F6), // Hardcoded: Figma #f3f4f6
            Color(0xFF6A7282), // Hardcoded: Figma #6a7282
            "알림"
        )

        NotificationStatus.EMERGENCY -> Triple(
            Color(0xFFFFE2E2), // Hardcoded: Figma #ffe2e2
            Color(0xFFC10007), // Hardcoded: Figma #c10007
            "긴급"
        )

        NotificationStatus.PINNED -> Triple(
            Color(0xFFDBEAFE), // Hardcoded: Figma #dbeafe
            Color(0xFF1447E6), // Hardcoded: Figma #1447e6
            "공지"
        )
    }

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationStatusChipNormalPreview() {
    PaTheme {
        NotificationStatusChip(status = NotificationStatus.NORMAL)
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationStatusChipEmergencyPreview() {
    PaTheme {
        NotificationStatusChip(status = NotificationStatus.EMERGENCY)
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationStatusChipPinnedPreview() {
    PaTheme {
        NotificationStatusChip(status = NotificationStatus.PINNED)
    }
}
