package io.github.helpingstar.protest_alert.feature.schedule.impl.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.helpingstar.protest_alert.core.designsystem.icon.PaIcons
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaTheme
import io.github.helpingstar.protest_alert.core.designsystem.theme.fontFamily

/**
 * Empty state container component
 *
 * Figma element name: EmptyStateContainer
 * Figma element type: Frame
 * Figma node-id: 365:1076
 *
 * Displays an empty state message when there are no schedules
 * for the selected regions in the last 7 days.
 *
 * Layout structure:
 * ```
 * ┌─────────────────────────────────────────────────────────┐
 * │                         64dp                            │
 * │                    ┌──────────┐                         │
 * │                    │ 📅❌ Icon │  60x60                  │
 * │                    └──────────┘                         │
 * │                         24dp                            │
 * │           최근 7일간 일정이 없습니다                    │
 * │                         24dp                            │
 * │       선택한 관심 지역(서울, 부산 외 3개)에              │
 * │           등록된 집회 일정이 없어요.                    │
 * └─────────────────────────────────────────────────────────┘
 * ```
 *
 * Dependencies: None (leaf component)
 *
 * @param followedRegionNames List of followed region names to display
 * @param modifier Optional modifier for the component
 */
@Composable
internal fun EmptyStateContainer(
    followedRegionNames: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(top = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Icon (60x60)
        Icon(
            painter = painterResource(id = PaIcons.EventBusyBorder),
            contentDescription = "No schedules",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(60.dp)
        )

        // Title
        Text(
            text = "최근 7일간 일정이 없습니다",
            style = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                lineHeight = 28.sp
            ),
            color = Color(0xFF171A1F) // Hardcoded: as specified in Figma
        )

        // Subtitle (dynamic region text)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formatRegionText(followedRegionNames),
                style = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                ),
                color = Color(0xFF6A7282), // Hardcoded: as specified in Figma
                textAlign = TextAlign.Center
            )
            Text(
                text = "등록된 집회 일정이 없어요.",
                style = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                ),
                color = Color(0xFF6A7282), // Hardcoded: as specified in Figma
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Formats the region text based on the number of followed regions.
 *
 * @param regions List of region names
 * @return Formatted string:
 *   - 0 regions: "선택한 관심 지역이 없습니다"
 *   - 1 region: "선택한 관심 지역(서울)에"
 *   - 2 regions: "선택한 관심 지역(서울, 부산)에"
 *   - 3+ regions: "선택한 관심 지역(서울, 부산 외 N개)에"
 */
private fun formatRegionText(regions: List<String>): String {
    return when {
        regions.isEmpty() -> "선택한 관심 지역이 없습니다"
        regions.size == 1 -> "선택한 관심 지역(${regions[0]})에"
        regions.size == 2 -> "선택한 관심 지역(${regions[0]}, ${regions[1]})에"
        else -> "선택한 관심 지역(${regions[0]}, ${regions[1]} 외 ${regions.size - 2}개)에"
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStateContainerPreview() {
    PaTheme {
        EmptyStateContainer(
            followedRegionNames = listOf("서울", "부산", "대구", "인천", "광주")
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStateContainerNoRegionsPreview() {
    PaTheme {
        EmptyStateContainer(
            followedRegionNames = emptyList()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStateContainerOneRegionPreview() {
    PaTheme {
        EmptyStateContainer(
            followedRegionNames = listOf("서울")
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStateContainerTwoRegionsPreview() {
    PaTheme {
        EmptyStateContainer(
            followedRegionNames = listOf("서울", "부산")
        )
    }
}
