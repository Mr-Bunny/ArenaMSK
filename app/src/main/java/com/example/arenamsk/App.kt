package com.example.arenamsk

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.example.arenamsk.utils.NotificationsUtils.CHANNEL_ID

/** Класс приложения */
class App : MultiDexApplication() {

    companion object {
        private lateinit var instance: App

        /** Метод для получения контекста приложения из люой точки приложения */
        fun appContext() = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        //Create the NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}