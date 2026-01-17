package io.github.helpingstar.protest_alert.feature.schedule.impl.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaColor
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaTheme

/**
 * Jurisdiction chip component
 *
 * Figma element name: 관할구역 칩
 * Figma element type: Frame
 * Figma node-id: 141-702 (part of ScheduleItem)
 *
 * Displays a jurisdiction name with white background and gray border.
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
    Surface(
        modifier = modifier.height(22.dp),
        shape = RoundedCornerShape(4.dp),
        color = Color.White,
        border = BorderStroke(1.dp, PaColor.borderDefault)
    ) {
        Text(
            text = jurisdiction,
            style = MaterialTheme.typography.bodyMedium,
            color = PaColor.textSecondary,
            modifier = Modifier.padding(horizontal = 13.dp, vertical = 3.dp)
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
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        jurisdictions.forEach { jurisdiction ->
            JurisdictionChip(jurisdiction = jurisdiction)
        }
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
