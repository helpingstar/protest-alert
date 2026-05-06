package io.github.helpingstar.protest_alert.core.notifications

import android.Manifest.permission
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.helpingstar.protest_alert.core.model.data.ProtestResource
import javax.inject.Inject
import javax.inject.Singleton

private const val TARGET_ACTIVITY_NAME = "io.github.helpingstar.protest_alert.MainActivity"
private const val PROTEST_NOTIFICATION_REQUEST_CODE = 0
private const val PROTEST_NOTIFICATION_CHANNEL_ID = "protest_updates"

@Singleton
internal class SystemTrayNotifier @Inject constructor(
    @ApplicationContext private val context: Context,
) : Notifier {
    override fun postProtestNotifications(
        protestResources: List<ProtestResource>,
    ) = with(context) {
        if (checkSelfPermission(this, permission.POST_NOTIFICATIONS) != PERMISSION_GRANTED) {
            return
        }

        val protestResourcesByRegion = protestResources.groupBy(ProtestResource::region)

        val notificationManager = NotificationManagerCompat.from(this)

        protestResourcesByRegion.forEach { (region, regionProtestResources) ->
            val title = getString(
                R.string.core_notifications_protest_region_update_title,
                region,
                regionProtestResources.size,
            )
            val contentText = getString(R.string.core_notifications_protest_region_update_text)

            val notification = createProtestNotification {
                setSmallIcon(R.drawable.core_notifications_ic_pa_notification)
                    .setContentTitle(title)
                    .setContentText(contentText)
                    .setContentIntent(schedulePendingIntent())
                    .setAutoCancel(true)
            }

            notificationManager.notify(
                region.hashCode(),
                notification,
            )
        }
    }
}

private fun Context.createProtestNotification(
    block: NotificationCompat.Builder.() -> Unit,
): Notification {
    ensureNotificationChannelExists()
    return NotificationCompat.Builder(
        this,
        PROTEST_NOTIFICATION_CHANNEL_ID,
    )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .apply(block)
        .build()
}

private fun Context.ensureNotificationChannelExists() {
    if (VERSION.SDK_INT < VERSION_CODES.O) return

    val channel = NotificationChannel(
        PROTEST_NOTIFICATION_CHANNEL_ID,
        getString(R.string.core_notifications_protest_notification_channel_name),
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description =
            getString(R.string.core_notifications_protest_notification_channel_description)
    }

    NotificationManagerCompat.from(this).createNotificationChannel(channel)
}

private fun Context.schedulePendingIntent(): PendingIntent? = PendingIntent.getActivity(
    this,
    PROTEST_NOTIFICATION_REQUEST_CODE,
    Intent().apply {
        action = Intent.ACTION_MAIN
        component = ComponentName(
            packageName,
            TARGET_ACTIVITY_NAME,
        )
    },
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
)
