package io.github.helpingstar.protest_alert.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaTheme
import io.github.helpingstar.protest_alert.core.designsystem.theme.fontFamily

/**
 * Notification card component
 *
 * Figma element name: Container
 * Figma element type: Component
 * Figma node-id: 274:2335, 277:1430
 *
 * Displays a notification card with status badge, date, title, and body.
 * Supports 3 status types (Normal, Emergency, Pinned) with read/unread states.
 * Can be expanded or collapsed (body shows 1 line with ellipsis when collapsed).
 *
 * Dependencies:
 * - [NotificationStatusChip]
 *
 * @param status The notification status type (NORMAL, EMERGENCY, PINNED)
 * @param title The notification title
 * @param body The notification body text
 * @param date The notification date string (e.g., "2024.07.26")
 * @param isRead Whether the notification has been read
 * @param isExpanded Whether the card is expanded (shows full body)
 * @param onCardClick Callback when card is clicked (for expand toggle + read marking)
 * @param modifier Optional modifier for the component
 */
@Composable
fun NotificationCard(
    status: NotificationStatus,
    title: String,
    body: String,
    date: String,
    isRead: Boolean,
    isExpanded: Boolean,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = getCardColors(status, isRead)

    Card(
        onClick = onCardClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.backgroundColor
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header row: Badge + Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NotificationStatusChip(status = status)
                Text(
                    text = date,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = Color(0xFF99A1AF) // Hardcoded: Figma #99a1af
                )
            }

            // Title
            Text(
                text = title,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                color = colors.titleColor
            )

            // Body
            Text(
                text = body,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 23.sp,
                color = colors.bodyColor,
                maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Color configuration for NotificationCard based on status and read state.
 */
private data class NotificationCardColors(
    val backgroundColor: Color,
    val borderColor: Color,
    val titleColor: Color,
    val bodyColor: Color
)

/**
 * Returns appropriate colors based on notification status and read state.
 * - NORMAL: Has read/unread distinction
 * - EMERGENCY/PINNED: Same colors regardless of read state
 */
private fun getCardColors(status: NotificationStatus, isRead: Boolean): NotificationCardColors {
    return when (status) {
        NotificationStatus.NORMAL -> {
            if (isRead) {
                NotificationCardColors(
                    backgroundColor = Color(0xFFF9FAFB), // Hardcoded: Figma #f9fafb
                    borderColor = Color(0x80F9FAFB),     // Hardcoded: Figma rgba(249,250,251,0.5)
                    titleColor = Color(0xFF4A5565),      // Hardcoded: Figma #4a5565
                    bodyColor = Color(0xFF99A1AF)        // Hardcoded: Figma #99a1af
                )
            } else {
                NotificationCardColors(
                    backgroundColor = Color.White,
                    borderColor = Color(0xFFE5E7EB),     // Hardcoded: Figma #e5e7eb
                    titleColor = Color(0xFF101828),      // Hardcoded: Figma #101828
                    bodyColor = Color(0xFF364153)        // Hardcoded: Figma #364153
                )
            }
        }

        NotificationStatus.EMERGENCY -> {
            NotificationCardColors(
                backgroundColor = Color(0xFFFEF2F2), // Hardcoded: Figma #fef2f2
                borderColor = Color(0x80FEF2F2),     // Hardcoded: Figma rgba(254,242,242,0.5)
                titleColor = Color(0xFFC10007),      // Hardcoded: Figma #c10007
                bodyColor = Color(0xFF364153)        // Hardcoded: Figma #364153
            )
        }

        NotificationStatus.PINNED -> {
            NotificationCardColors(
                backgroundColor = Color(0xFFEFF6FF), // Hardcoded: Figma #eff6ff
                borderColor = Color(0x80EFF6FF),     // Hardcoded: Figma rgba(239,246,255,0.5)
                titleColor = Color(0xFF101828),      // Hardcoded: Figma #101828
                bodyColor = Color(0xFF364153)        // Hardcoded: Figma #364153
            )
        }
    }
}

// region Previews - Collapsed (default)

@Preview(showBackground = true)
@Composable
private fun NotificationCardNormalCollapsedPreview() {
    PaTheme {
        NotificationCard(
            status = NotificationStatus.NORMAL,
            title = "장소 변경 안내",
            body = "금일 예정된 광화문 집회 장소가 변경되었습니다. 확인 부탁드립니다.",
            date = "2024.07.26",
            isRead = false,
            isExpanded = false,
            onCardClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationCardNormalReadCollapsedPreview() {
    PaTheme {
        NotificationCard(
            status = NotificationStatus.NORMAL,
            title = "집회 장소 변경 안내",
            body = "금일 예정된 광화문 집회 장소가 변경되었습니다. 확인 부탁드립니다.",
            date = "2024.07.26",
            isRead = true,
            isExpanded = false,
            onCardClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationCardEmergencyCollapsedPreview() {
    PaTheme {
        NotificationCard(
            status = NotificationStatus.EMERGENCY,
            title = "장소 변경 안내",
            body = "금일 예정된 광화문 집회 장소가 변경되었습니다. 확인 부탁드립니다.",
            date = "2024.07.26",
            isRead = false,
            isExpanded = false,
            onCardClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationCardPinnedCollapsedPreview() {
    PaTheme {
        NotificationCard(
            status = NotificationStatus.PINNED,
            title = "집회 장소 변경 안내",
            body = "금일 예정된 광화문 집회 장소가 변경되었습니다. 확인 부탁드립니다.",
            date = "2024.07.26",
            isRead = false,
            isExpanded = false,
            onCardClick = {}
        )
    }
}

// endregion

// region Previews - Expanded

@Preview(showBackground = true)
@Composable
private fun NotificationCardNormalExpandedPreview() {
    PaTheme {
        NotificationCard(
            status = NotificationStatus.NORMAL,
            title = "장소 변경 안내",
            body = "금일 예정된 광화문 집회 장소가 변경되었습니다. 확인 부탁드립니다.",
            date = "2024.07.26",
            isRead = false,
            isExpanded = true,
            onCardClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationCardEmergencyExpandedPreview() {
    PaTheme {
        NotificationCard(
            status = NotificationStatus.EMERGENCY,
            title = "장소 변경 안내",
            body = "금일 예정된 광화문 집회 장소가 변경되었습니다. 확인 부탁드립니다.",
            date = "2024.07.26",
            isRead = false,
            isExpanded = true,
            onCardClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationCardPinnedExpandedPreview() {
    PaTheme {
        NotificationCard(
            status = NotificationStatus.PINNED,
            title = "집회 장소 변경 안내",
            body = "금일 예정된 광화문 집회 장소가 변경되었습니다. 확인 부탁드립니다.",
            date = "2024.07.26",
            isRead = false,
            isExpanded = true,
            onCardClick = {}
        )
    }
}

// endregion
