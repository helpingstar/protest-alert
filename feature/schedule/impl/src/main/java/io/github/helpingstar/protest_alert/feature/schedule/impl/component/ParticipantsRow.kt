package io.github.helpingstar.protest_alert.feature.schedule.impl.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
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
import io.github.helpingstar.protest_alert.core.designsystem.theme.fontFamily

/**
 * Participants count row component
 *
 * Figma element name: Container
 * Figma element type: Frame
 * Figma node-id: 192:770
 *
 * Displays an icon and participant count text in a horizontal row.
 *
 * Dependencies: None (leaf component)
 *
 * @param participantCount The number of participants to display
 * @param modifier Optional modifier for the component
 */
@Composable
fun ParticipantsRow(
    participantCount: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon - node 192:802
        Icon(
            imageVector = PaIcons.GroupBorder,
            contentDescription = "Participants icon",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )

        // Text - node 192:776
        Text(
            text = "참여자 $participantCount",
            style = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ParticipantsRowPreview() {
    ParticipantsRow(
        participantCount = "50,000명"
    )
}