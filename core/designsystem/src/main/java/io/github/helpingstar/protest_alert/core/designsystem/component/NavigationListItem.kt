package io.github.helpingstar.protest_alert.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.helpingstar.protest_alert.core.designsystem.icon.PaIcons
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaColor
import io.github.helpingstar.protest_alert.core.designsystem.theme.fontFamily

/**
 * NavigationListItem component that displays a clickable list item with an icon,
 * text label, and trailing arrow indicator for navigation.
 *
 * @param text The text label to display
 * @param leadingIcon The icon to display on the left side
 * @param onClick Callback invoked when the item is clicked
 * @param modifier Optional modifier for this component
 */
@Composable
fun NavigationListItem(
    text: String,
    leadingIcon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp),
        color = PaColor.BackgroundSurface,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, PaColor.BorderDefault),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 17.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Leading section: Icon + Text
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Icon container
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = PaColor.AccentPrimary,
                    )
                }

                Text(
                    text = text,
                    style = TextStyle(
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = PaColor.PrimaryText,
                    ),
                )
            }

            // Trailing arrow icon
            Icon(
                imageVector = PaIcons.ArrowForwardIOS,
                contentDescription = "이동",
                modifier = Modifier.size(20.dp),
                tint = PaColor.SecondaryText,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NavigationListItemPreview() {
    NavigationListItem(
        text = "개발자에게 바란다",
        leadingIcon = PaIcons.Info,
        onClick = {},
    )
}
