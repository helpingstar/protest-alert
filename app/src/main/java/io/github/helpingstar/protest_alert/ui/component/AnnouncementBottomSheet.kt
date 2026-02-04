package io.github.helpingstar.protest_alert.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.helpingstar.protest_alert.core.designsystem.icon.PaIcons
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaTheme
import io.github.helpingstar.protest_alert.core.designsystem.theme.fontFamily

/**
 * Announcement bottom sheet component
 *
 * Figma element name: Container
 * Figma element type: Frame
 * Figma node-id: 271:1419
 *
 * Displays a modal bottom sheet with a list of announcements.
 * Supports Emergency, Pinned, and Normal announcement types.
 * Cards can be expanded to show full body text.
 *
 * Dependencies:
 * - [NotificationCard]
 * - [NotificationStatusChip]
 *
 * @param announcements List of announcements to display
 * @param onDismiss Callback when bottom sheet is dismissed
 * @param onMarkAllRead Callback when "모두 읽음" button is clicked
 * @param onAnnouncementClick Callback when an announcement card is clicked (for read marking)
 * @param modifier Optional modifier for the component
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementBottomSheet(
    announcements: List<AnnouncementUiModel>,
    onDismiss: () -> Unit,
    onMarkAllRead: () -> Unit,
    onAnnouncementClick: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()

    // Internal state for expanded cards
    var expandedCardIds by remember { mutableStateOf(setOf<String>()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        modifier = modifier
    ) {
        Column {
            // Header
            AnnouncementHeader(
                onMarkAllRead = onMarkAllRead,
                onClose = onDismiss
            )

            // Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x80F9FAFB)), // Hardcoded: Figma rgba(249,250,251,0.5)
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 32.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = announcements,
                    key = { it.id }
                ) { announcement ->
                    val isExpanded = expandedCardIds.contains(announcement.id)

                    NotificationCard(
                        status = announcement.status,
                        title = announcement.title,
                        body = announcement.body,
                        date = announcement.date,
                        isRead = announcement.isRead,
                        isExpanded = isExpanded,
                        onCardClick = {
                            // Toggle expand state
                            expandedCardIds = if (isExpanded) {
                                expandedCardIds - announcement.id
                            } else {
                                expandedCardIds + announcement.id
                            }
                            // Notify for read marking
                            onAnnouncementClick(announcement.id)
                        }
                    )
                }
            }
        }
    }
}

/**
 * Header component for AnnouncementBottomSheet
 *
 * Figma node-id: 271:1422
 *
 * Contains title "공지사항", "모두 읽음" button, and close icon.
 */
@Composable
private fun AnnouncementHeader(
    onMarkAllRead: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(65.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFF3F4F6), // Hardcoded: Figma #f3f4f6
                shape = RectangleShape
            )
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        // Title
        Text(
            text = "공지사항",
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 28.sp,
            color = Color(0xFF101828) // Hardcoded: Figma #101828
        )

        // Right side: "모두 읽음" + Close icon
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "모두 읽음",
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color(0xFF364153), // Hardcoded: Figma #364153
                modifier = Modifier.clickable { onMarkAllRead() }
            )

            IconButton(
                onClick = onClose,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = PaIcons.Close,
                    contentDescription = "닫기",
                    tint = Color(0xFF364153) // Hardcoded: Figma #364153
                )
            }
        }
    }
}

// region Previews

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun AnnouncementBottomSheetPreview() {
    val dummyAnnouncements = listOf(
        AnnouncementUiModel(
            id = "1",
            status = NotificationStatus.EMERGENCY,
            title = "긴급 공지",
            body = "금일 예정된 광화문 집회 장소가 변경되었습니다. 확인 부탁드립니다.",
            date = "2024.07.26",
            isRead = false
        ),
        AnnouncementUiModel(
            id = "2",
            status = NotificationStatus.PINNED,
            title = "중요 안내",
            body = "앱 업데이트가 필요합니다. 최신 버전으로 업데이트해주세요.",
            date = "2024.07.25",
            isRead = false
        ),
        AnnouncementUiModel(
            id = "3",
            status = NotificationStatus.NORMAL,
            title = "일반 공지",
            body = "서비스 점검 안내입니다. 점검 시간: 02:00 ~ 04:00",
            date = "2024.07.24",
            isRead = true
        ),
        AnnouncementUiModel(
            id = "4",
            status = NotificationStatus.NORMAL,
            title = "업데이트 안내",
            body = "새로운 기능이 추가되었습니다. 지금 확인해보세요!",
            date = "2024.07.23",
            isRead = false
        )
    )

    PaTheme {
        AnnouncementBottomSheet(
            announcements = dummyAnnouncements,
            onDismiss = {},
            onMarkAllRead = {},
            onAnnouncementClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AnnouncementHeaderPreview() {
    PaTheme {
        AnnouncementHeader(
            onMarkAllRead = {},
            onClose = {}
        )
    }
}

// endregion
