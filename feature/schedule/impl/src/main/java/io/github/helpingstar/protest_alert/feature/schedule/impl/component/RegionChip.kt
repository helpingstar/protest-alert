package io.github.helpingstar.protest_alert.feature.schedule.impl.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaColor
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaTheme

/**
 * Region chip component
 *
 * Figma element name: 지역 칩
 * Figma element type: Frame
 * Figma node-id: 141-702 (part of ScheduleItem)
 *
 * Displays the region name with accent primary background (10% opacity).
 *
 * Dependencies: None (leaf component)
 *
 * @param region Region name to display
 * @param modifier Optional modifier for the component
 */
@Composable
internal fun RegionChip(
    region: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = PaColor.accentPrimary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(11.dp)
            )
            .padding(horizontal = 12.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = region,
            style = MaterialTheme.typography.bodyMedium,
            color = PaColor.accentPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegionChipPreview() {
    PaTheme {
        RegionChip(region = "서울")
    }
}
