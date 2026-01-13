package io.github.helpingstar.protest_alert.feature.settings.impl.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.helpingstar.protest_alert.core.designsystem.icon.PaIcons
import io.github.helpingstar.protest_alert.core.designsystem.theme.PaColor
import io.github.helpingstar.protest_alert.core.designsystem.theme.fontFamily

/**
 * Feedback modal dialog for sending feedback to developers
 *
 * Figma element name: 개발자에게 바란다
 * Figma element type: Component Set
 * Figma node-id: 141:964 (input), 151:548 (success)
 *
 * Displays a modal with:
 * - Input state: Header, description, text area, buttons
 * - Success state: Checkmark icon with thank you message
 *
 * @param feedbackText Current feedback text
 * @param onFeedbackTextChange Callback when feedback text changes
 * @param onDismiss Callback when modal is dismissed (close or cancel)
 * @param onSend Callback when send button is clicked
 * @param isSuccess Whether feedback was successfully sent
 * @param modifier Optional modifier for the component
 */
@Composable
fun FeedbackModal(
    feedbackText: String,
    onFeedbackTextChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSend: () -> Unit,
    isSuccess: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val isEditing = feedbackText.isNotEmpty()

    Surface(
        modifier = modifier
            .width(350.dp)
            .height(385.dp)
            .shadow(
                elevation = 40.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.25f),
                spotColor = Color.Black.copy(alpha = 0.25f),
            ),
        shape = RoundedCornerShape(16.dp),
        color = PaColor.backgroundSurface,
    ) {
        if (isSuccess) {
            // Success state
            FeedbackSuccessContent()
        } else {
            // Input state
            Column {
                FeedbackModalHeader(
                    onDismiss = onDismiss,
                )

                FeedbackModalContent(
                    feedbackText = feedbackText,
                    onFeedbackTextChange = onFeedbackTextChange,
                )

                FeedbackModalButtons(
                    isEditing = isEditing,
                    onCancel = onDismiss,
                    onSend = onSend,
                )
            }
        }
    }
}

/**
 * Success content for FeedbackModal
 *
 * Figma element name: Success
 * Figma element type: Frame
 * Figma node-id: 151:548
 *
 * Displays checkmark icon and thank you message
 */
@Composable
private fun FeedbackSuccessContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Blue circle with checkmark
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        color = PaColor.accentPrimary,
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = PaIcons.Check,
                    contentDescription = "성공",
                    modifier = Modifier.size(54.dp),
                    tint = Color.White,
                )
            }

            // Thank you message
            Text(
                text = "소중한 의견 감사드립니다",
                style = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    lineHeight = 27.sp,
                    color = PaColor.textSecondary,
                ),
            )
        }
    }
}

@Composable
private fun FeedbackModalHeader(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = PaColor.borderDefault,
                shape = RoundedCornerShape(
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp,
                ),
            )
            .padding(
                20.dp,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "개발자에게 바란다",
            style = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 27.sp,
                color = PaColor.textPrimary,
            ),
        )
        IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(24.dp),
        ) {
            Icon(
                imageVector = PaIcons.Close,
                contentDescription = "닫기",
                modifier = Modifier.size(20.dp),
                tint = PaColor.textPrimary,
            )
        }
    }
}

@Composable
private fun FeedbackModalContent(
    feedbackText: String,
    onFeedbackTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Description
        Text(
            text = "개선이 필요한 부분이나 추가했으면 하는 기능을 알려주세요.",
            style = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = PaColor.textSecondary,
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        // Text area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .border(
                    width = 1.dp,
                    color = PaColor.borderDefault,
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(12.dp),
        ) {
            BasicTextField(
                value = feedbackText,
                onValueChange = onFeedbackTextChange,
                textStyle = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = PaColor.textPrimary,
                ),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    Box {
                        if (feedbackText.isEmpty()) {
                            Text(
                                text = "피드백을 입력해주세요...",
                                style = TextStyle(
                                    fontFamily = fontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    lineHeight = 21.sp,
                                    color = PaColor.textPlaceholder,
                                ),
                            )
                        }
                        innerTextField()
                    }
                },
            )
        }
    }
}

@Composable
private fun ColumnScope.FeedbackModalButtons(
    isEditing: Boolean,
    onCancel: () -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(9.dp),
        verticalAlignment = Alignment.Top,
    ) {
        // Cancel button
        CancelButton(onClick = onCancel)

        // Send button
        SendButton(
            isEditing = isEditing,
            onClick = onSend,
        )
    }
}

@Composable
private fun RowScope.CancelButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .weight(1f)
            .height(46.dp),
        shape = RoundedCornerShape(8.dp),
        color = PaColor.backgroundSurface,
        border = BorderStroke(1.dp, PaColor.borderDefault),
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "취소",
                style = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = PaColor.textPrimary,
                ),
            )
        }
    }
}

@Composable
private fun RowScope.SendButton(
    isEditing: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = { if (isEditing) onClick() },
        modifier = modifier
            .weight(1f)
            .height(46.dp),
        shape = RoundedCornerShape(8.dp),
        color = if (isEditing) PaColor.accentPrimary else PaColor.backgroundDisabled,
        enabled = isEditing,
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "전송",
                style = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = if (isEditing) PaColor.textOnAccent else PaColor.textDisabled,
                ),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
private fun FeedbackModalDefaultPreview() {
    var feedbackText by remember { mutableStateOf("") }
    FeedbackModal(
        feedbackText = feedbackText,
        onFeedbackTextChange = { feedbackText = it },
        onDismiss = {},
        onSend = {},
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
private fun FeedbackModalEditingPreview() {
    var feedbackText by remember { mutableStateOf("앱이 정말 유용해요! 다만 알림 설정 기능이 추가되면 좋겠습니다.") }
    FeedbackModal(
        feedbackText = feedbackText,
        onFeedbackTextChange = { feedbackText = it },
        onDismiss = {},
        onSend = {},
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
private fun FeedbackModalSuccessPreview() {
    FeedbackModal(
        feedbackText = "",
        onFeedbackTextChange = {},
        onDismiss = {},
        onSend = {},
        isSuccess = true,
    )
}