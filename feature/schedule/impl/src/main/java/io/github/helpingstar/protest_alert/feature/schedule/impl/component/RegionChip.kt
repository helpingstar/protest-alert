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
import androidx.compose.ui.unit.sp
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaColor
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaTheme
import io.github.helpingstar.protest_alert.core.designsystem.theme.fontFamily

@Composable
internal fun RegionChip(
    region: String,
    modifier: Modifier = Modifier
) {
    val mainColor = MaterialTheme.colorScheme.primary
    Box(
        modifier = modifier
            .background(
                color = mainColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = region,
            fontFamily = fontFamily,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            style = MaterialTheme.typography.bodyMedium,
            color = mainColor.copy()
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
