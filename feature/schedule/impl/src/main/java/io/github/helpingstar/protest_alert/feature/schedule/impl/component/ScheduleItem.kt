package io.github.helpingstar.protest_alert.feature.schedule.impl.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaColor
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaTheme
import io.github.helpingstar.protest_alert.core.model.data.ProtestResource
import io.github.helpingstar.protest_alert.core.model.data.UserData
import io.github.helpingstar.protest_alert.core.model.data.UserProtestResource
import io.github.helpingstar.protest_alert.feature.schedule.impl.expToLinear
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.Locale
import kotlin.time.Instant

/**
 * Schedule item card component
 *
 * Figma element name: 집회 일정 아이템
 * Figma element type: Frame
 * Figma node-id: 141-702
 *
 * Displays a single protest schedule item with time, region, location,
 * participants count, and jurisdiction chips.
 *
 * Dependencies:
 * - [RegionChip]
 * - [JurisdictionChip]
 * - [JurisdictionChipRow]
 *
 * Layout structure:
 * ```
 * ┌─────────────────────────────────────────────────────────┐
 * │  09:00    서울고용노동청 → 건보공단 강동지사 <장교동 등>  │
 * │    ~      👥 참여자 7,000명                             │
 * │  12:00    [마포] [강남] [홍대] [이태원]                  │
 * │  [서울]   [압구정]                                      │
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
    val (startTime, endTime) = formatTimeRangePair(protest)
    val calculatedAlpha = expToLinear(protest.participants ?: 0)
    val jurisdictions = parseJurisdictions(protest.additionalInfo)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = PaColor.surfaceCard.copy(alpha = calculatedAlpha.toFloat())
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Left column: Time + RegionChip
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "$startTime\n~\n$endTime",
                    style = MaterialTheme.typography.titleSmall,
                    color = PaColor.textPrimary,
                    textAlign = TextAlign.Center
                )
                RegionChip(region = protest.region)
            }

            // Right column: Location + Participants + Jurisdiction chips
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Location text
                Text(
                    text = protest.location ?: "-",
                    style = MaterialTheme.typography.bodyLarge,
                    color = PaColor.textPrimary
                )

                // Participants row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = PaColor.textSecondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "참여자 ${formatParticipantsWithComma(protest.participants)}명",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PaColor.textSecondary
                    )
                }

                // Jurisdiction chips
                JurisdictionChipRow(jurisdictions = jurisdictions)
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

private fun formatTimeRangePair(protest: UserProtestResource): Pair<String, String> {
    val timeZone = TimeZone.of("Asia/Seoul")

    val startTime = protest.startAt?.let {
        val localDateTime = it.toLocalDateTime(timeZone)
        String.format(Locale.ROOT, "%02d:%02d", localDateTime.hour, localDateTime.minute)
    } ?: "--:--"

    val endTime = protest.endAt?.let {
        val localDateTime = it.toLocalDateTime(timeZone)
        String.format(Locale.ROOT, "%02d:%02d", localDateTime.hour, localDateTime.minute)
    } ?: "--:--"

    return Pair(startTime, endTime)
}

private fun formatParticipantsWithComma(count: Int?): String {
    return count?.let {
        String.format(Locale.ROOT, "%,d", it)
    } ?: "0"
}

@Preview(showBackground = true)
@Composable
private fun ScheduleItemPreview() {
    val userData = UserData(followedRegions = setOf("서울"), shouldHideOnboarding = true)
    val protest = ProtestResource(
        id = 1L,
        date = LocalDate(2025, 1, 15),
        startAt = Instant.parse("2025-01-15T00:00:00Z"),
        endAt = Instant.parse("2025-01-15T03:00:00Z"),
        location = "서울고용노동청 → 건보공단 강동지사 <장교동 등>",
        participants = 7000,
        additionalInfo = mapOf("jurisdiction" to "중 부\n남대문\n성 동\n광 진\n강 동"),
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
