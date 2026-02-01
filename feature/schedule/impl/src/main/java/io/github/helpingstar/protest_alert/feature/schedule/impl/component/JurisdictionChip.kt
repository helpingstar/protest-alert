package io.github.helpingstar.protest_alert.feature.schedule.impl.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import io.github.helpingstar.protest_alert.core.designsystem.icon.PaIcons
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaTheme
import io.github.helpingstar.protest_alert.core.designsystem.theme.fontFamily

/**
 * Jurisdiction chip component
 *
 * Figma element name: Text
 * Figma element type: Frame
 * Figma node-id: 193:819
 *
 * Displays a jurisdiction name with surface container background.
 *
 * Dependencies: None (leaf component)
 *
 * @param jurisdiction Jurisdiction name to display
 * @param modifier Optional modifier for the component
 */
@Composable
internal fun JurisdictionChip(
    jurisdiction: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = jurisdiction,
            style = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/**
 * Row of jurisdiction chips with flow layout
 *
 * Figma element name: 관할구역 칩 목록
 * Figma element type: Frame
 * Figma node-id: 141-702 (part of ScheduleItem)
 *
 * Displays multiple jurisdiction chips in a wrapping flow layout.
 *
 * Dependencies:
 * - [JurisdictionChip]
 *
 * @param jurisdictions List of jurisdiction names to display
 * @param modifier Optional modifier for the component
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun JurisdictionChipRow(
    jurisdictions: List<String>,
    modifier: Modifier = Modifier
) {
    if (jurisdictions.isEmpty()) return

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        jurisdictions.forEach { jurisdiction ->
            JurisdictionChip(jurisdiction = jurisdiction.replace(" ", ""))
        }
    }
}

/**
 * Jurisdiction row with icon and chips
 *
 * Figma element name: Frame
 * Figma element type: Frame
 * Figma node-id: 192:793
 *
 * Displays a shield icon followed by jurisdiction chips in a wrapping row.
 *
 * Dependencies:
 * - [JurisdictionChip]
 *
 * @param jurisdictions List of jurisdiction names to display
 * @param modifier Optional modifier for the component
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JurisdictionRow(
    jurisdictions: List<String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Shield icon - node 192:799
        Icon(
            imageVector = PaIcons.ShieldBorder,
            contentDescription = "Jurisdiction icon",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )

        JurisdictionChipRow(jurisdictions = jurisdictions)
    }
}

@Preview(showBackground = true)
@Composable
private fun JurisdictionChipPreview() {
    PaTheme {
        JurisdictionChip(jurisdiction = "마포")
    }
}

@Preview(showBackground = true)
@Composable
private fun JurisdictionChipRowPreview() {
    PaTheme {
        JurisdictionChipRow(
            jurisdictions = listOf("마포", "강남", "홍대", "이태원", "압구정")
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun JurisdictionRowPreview() {
    PaTheme {
        JurisdictionRow(
            jurisdictions = listOf("중부", "서대문", "중로", "종로")
        )
    }
}
