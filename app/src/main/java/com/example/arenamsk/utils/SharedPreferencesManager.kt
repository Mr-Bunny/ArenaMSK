package com.example.arenamsk.utils

import android.app.Notification
import android.content.Context
import android.content.SharedPreferences
import com.example.arenamsk.App

/** Класс для работы с SharedPreferences */
class SharedPreferenceManager private constructor() {

    companion object {
        private const val PREFS_FILENAME = "com.eventtime.prefs"

        @Volatile
        private var prefsManager: SharedPreferenceManager? = null

        private lateinit var prefs: SharedPreferences

        fun getInstance(): SharedPreferenceManager =
            prefsManager ?: synchronized(this) {
                prefsManager ?: SharedPreferenceManager().also {
                    prefs = createPrefs()
                }
            }

        private fun createPrefs(): SharedPreferences =
            App.appContext().getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    }

    fun saveValue(key: String, value: Any?) {
        prefs[key] = value
    }

    fun getBooleanValue(key: String, defaultValue: Boolean) = prefs[key, defaultValue]

    fun getStringValue(key: String, defaultValue: String) = prefs[key, defaultValue]

    fun getIntValue(key: String, defaultValue: Int) = prefs[key, defaultValue]

    fun getFloatValue(key: String, defaultValue: Float) = prefs[key, defaultValue]

    fun getLongValue(key: String, defaultValue: Long) = prefs[key, defaultValue]

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    /**
     * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
     */
    private operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    /**
     * finds value on given key.
     * [T] is the type of value
     * @param defaultValue optional default value - will take null for strings,
     * false for bool and -1 for numeric values if [defaultValue] is not specified
     */
    inline operator fun <reified T : Any> SharedPreferences.get(
        key: String,
        defaultValue: T? = null
    ): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    object KEY {
        const val IS_USER_AUTHORIZED = "is_user_authorized"
        const val IS_USER_DEFAULT = "is_user_default"
        const val POLITIC_ACCEPTED = "politic_accepted"
        const val USER_EMAIL = "email"
        const val AUTH_TOKEN = "auth_token"
        const val REFRESH_TOKEN = "refresh_token"
        const val EXPIRED_IN = "expired_in"
        const val NOTIFICATION_IS_ENABLED = "notifications_is_enabled"
        const val NOTIFICATION_TIME = "notifications_time"
        const val FCM_TOKEN = "fcm_token"
        const val FCM_TOKEN_UPDATED = "fcm_token_updated"
    }
}