package com.arenabooking.arenamsk.utils

import android.app.Notification
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.arenabooking.arenamsk.App
import com.arenabooking.arenamsk.R
import com.arenabooking.arenamsk.network.utils.AuthUtils

/** Класс для работы с уведомлениями */
object NotificationsUtils {

    const val CHANNEL_ID = "4815162342"
    private const val NOTIFY_ID = 123

    /** Показывем уведомления, если они включены
     * @param notificationTitle - Заголовок уведомления
     * @param notificationText - Текст уведомления
     * @param notificationHours - Сколько часов осталось до начала брони */
    fun showNotification(notificationTitle: String, notificationText: String, notificationHours: Int) {
        //Если пользователь пропускал регистрацию или не авторизировался - уведомление не показываем
        if (AuthUtils.isUserDefault() || !AuthUtils.isUserAuthorized()) return

        if (SharedPreferenceManager.getInstance()
                .getBooleanValue(SharedPreferenceManager.KEY.NOTIFICATION_IS_ENABLED, false) == true
        ) {
            val hours = SharedPreferenceManager.getInstance()
                .getIntValue(SharedPreferenceManager.KEY.NOTIFICATION_TIME, 0)

            //Если уведомление пришло не за то время, за которое должно (например мы в настройках выбрали
            // показывать уведомления за 1 час, а нам пришло за 12 часов, то не показываем его)
            if (notificationHours != hours) {
                return
            }

            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(App.appContext(), CHANNEL_ID)
                    .setSmallIcon(getIcon())
                    .setContentTitle(notificationTitle)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(notificationText))
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