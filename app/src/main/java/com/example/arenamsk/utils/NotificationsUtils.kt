package com.example.arenamsk.utils

import android.app.Notification
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.arenamsk.App
import com.example.arenamsk.R
import com.example.arenamsk.network.utils.AuthUtils

/** Класс для работы с уведомлениями */
object NotificationsUtils {

    const val CHANNEL_ID = "4815162342"
    private const val NOTIFY_ID = 123

    /** Показывем уведомления, если они включены  */
    fun showNotification() {
        //Если пользователь пропускал регистрацию или не авторизировался - уведомление не показываем
        if (AuthUtils.isUserDefault() || !AuthUtils.isUserAuthorized()) return

        if (SharedPreferenceManager.getInstance()
                .getBooleanValue(SharedPreferenceManager.KEY.NOTIFICATION_IS_ENABLED, false) == true
        ) {
            val hours = SharedPreferenceManager.getInstance()
                .getIntValue(SharedPreferenceManager.KEY.NOTIFICATION_TIME, 0)
            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(App.appContext(), CHANNEL_ID)
                    .setSmallIcon(getIcon())
                    .setContentTitle("Напоминание")
                    .setStyle(NotificationCompat.BigTextStyle().bigText("Забронированная площадка будет доступна через ${hours}ч."))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setDefaults(Notification.DEFAULT_ALL)

            val notificationManager = NotificationManagerCompat.from(App.appContext())
            notificationManager.notify(NOTIFY_ID, builder.build())
        }
    }

    private fun getIcon() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        R.drawable.ic_notification
    } else R.drawable.notification
}