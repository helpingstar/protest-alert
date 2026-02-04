package io.github.helpingstar.protest_alert.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Settings
import io.github.helpingstar.protest_alert.core.designsystem.R


object PaIcons {
    val CalendarBorder = Icons.Outlined.CalendarMonth
    val Calendar = Icons.Rounded.CalendarMonth
    val SettingsBorder = Icons.Outlined.Settings
    val Settings = Icons.Rounded.Settings
    val Info = Icons.Outlined.Info
    val Lock = Icons.Outlined.Lock
    val ArrowForwardIOS = Icons.AutoMirrored.Outlined.ArrowForwardIos
    val Close = Icons.Outlined.Close
    val ChatBubble = Icons.Default.ChatBubble
    val Check = Icons.Rounded.Check

    val ScheduleRounded = Icons.Rounded.Schedule

    val GroupBorder = Icons.Outlined.Group

    val ShieldBorder = Icons.Outlined.Shield

    @DrawableRes
    val NotificationsBorder = R.drawable.ic_notifications_border

    @DrawableRes
    val NotificationsUnreadBorder = R.drawable.ic_notifications_unread_border

}