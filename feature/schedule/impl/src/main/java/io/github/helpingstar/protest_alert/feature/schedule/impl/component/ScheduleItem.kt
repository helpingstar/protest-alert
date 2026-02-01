package io.github.helpingstar.protest_alert.feature.schedule.impl.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaTheme
import io.github.helpingstar.protest_alert.core.designsystem.theme.fontFamily
import io.github.helpingstar.protest_alert.core.model.data.ProtestResource
import io.github.helpingstar.protest_alert.core.model.data.UserData
import io.github.helpingstar.protest_alert.core.model.data.UserProtestResource
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.Locale
import kotlin.time.Instant

/**
 * Schedule item card component
 *
 * Figma element name: RallyCard
 * Figma element type: Frame
 * Figma node-id: 192:756
 *
 * Displays a single protest schedule item with time chip, region chip,
 * location heading, participants row, and jurisdiction row.
 *
 * Dependencies:
 * - [TimeChip]
 * - [RegionChip]
 * - [ParticipantsRow]
 * - [JurisdictionRow]
 *
 * Layout structure:
 * ```
 * ┌─────────────────────────────────────────────────────────┐
 * │  [09:00 ~ 22:00]                              [서울]   │
 * │  서울광장 및 세종대로 일대                             │
 * │  ───────────────────────────────────────────────────   │
 * │  👥 참여자 50,000명                                    │
 * │  🛡️ [중부] [서대문] [중로] [종로]                     │
 * └─────────────────────────────────────────────────────────┘
 * ```
 *
 * @param protest Protest resource data to display
 * @param modifier Optional modifier for the component
 */
@Composable
internal fun ScheduleItem(
    protest: UserProtestResource,
    modifier: Modifier = Modifier
) {
    val timeRange = formatTimeRange(protest)
    val jurisdictions = parseJurisdictions(protest.additionalInfo)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Section 1: Top row with TimeChip and RegionChip, plus Location heading
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Row with TimeChip (left) and RegionChip (right)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TimeChip(time = timeRange)
                    RegionChip(region = protest.region)
                }

                // Location heading
                Text(
                    text = protest.location ?: "-",
                    style = TextStyle(
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        lineHeight = 23.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Divider
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline,
                thickness = 1.dp
            )

            // Section 2: Participants and Jurisdiction
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ParticipantsRow(
                    participantCount = formatParticipantsWithComma(protest.participants)
                )

                if (jurisdictions.isNotEmpty()) {
                    JurisdictionRow(jurisdictions = jurisdictions)
                }
            }
        }
    }
}

/**
 * Parses jurisdiction data from additionalInfo map.
 * The jurisdiction field contains newline-separated jurisdiction names.
 */
private fun parseJurisdictions(additionalInfo: Map<String, String>?): List<String> {
    return additionalInfo?.get("jurisdiction")
        ?.split("\n")
        ?.map { it.trim() }
        ?.filter { it.isNotEmpty() }
        ?: emptyList()
}

/**
 * Formats the time range for display in TimeChip.
 * Returns format: "HH:MM ~ HH:MM" (e.g., "09:00 ~ 22:00")
 */
private fun formatTimeRange(protest: UserProtestResource): String {
    val timeZone = TimeZone.of("Asia/Seoul")

    val startTime = protest.startAt?.let {
        val localDateTime = it.toLocalDateTime(timeZone)
        String.format(Locale.ROOT, "%02d:%02d", localDateTime.hour, localDateTime.minute)
    } ?: "--:--"

    val endTime = protest.endAt?.let {
        val localDateTime = it.toLocalDateTime(timeZone)
        String.format(Locale.ROOT, "%02d:%02d", localDateTime.hour, localDateTime.minute)
    } ?: "--:--"

    return "$startTime ~ $endTime"
}

/**
 * Formats participant count with thousand separators and "명" suffix.
 * Returns format: "N,NNN명" (e.g., "50,000명")
 */
private fun formatParticipantsWithComma(count: Int?): String {
    return count?.let {
        String.format(Locale.ROOT, "%,d명", it)
    } ?: "0명"
}

@Preview(showBackground = true)
@Composable
private fun ScheduleItemPreview() {
    val userData = UserData(followedRegions = setOf("서울"), shouldHideOnboarding = true)
    val protest = ProtestResource(
        id = 1L,
        date = LocalDate(2025, 1, 15),
        startAt = Instant.parse("2025-01-15T00:00:00Z"),  // 09:00 KST
        endAt = Instant.parse("2025-01-15T13:00:00Z"),    // 22:00 KST
        location = "서울광장 및 세종대로 일대",
        participants = 50000,
        additionalInfo = mapOf("jurisdiction" to "중부\n서대문\n중로\n종로"),
        createdAt = Instant.DISTANT_PAST,
        region = "서울",
        updatedAt = Instant.DISTANT_PAST
    )

    PaTheme {
        ScheduleItem(protest = UserProtestResource(protest, userData))
    }
}

@Preview(showBackground = true)
@Composable
private fun ScheduleItemWithoutJurisdictionPreview() {
    val userData = UserData(followedRegions = setOf("서울"), shouldHideOnboarding = true)
    val protest = ProtestResource(
        id = 2L,
        date = LocalDate(2025, 1, 15),
        startAt = Instant.parse("2025-01-15T05:00:00Z"),
        endAt = Instant.parse("2025-01-15T07:00:00Z"),
        location = "광화문 광장",
        participants = 3000,
        additionalInfo = null,
        createdAt = Instant.DISTANT_PAST,
        region = "서울",
        updatedAt = Instant.DISTANT_PAST
    )

    PaTheme {
        ScheduleItem(protest = UserProtestResource(protest, userData))
    }
}
