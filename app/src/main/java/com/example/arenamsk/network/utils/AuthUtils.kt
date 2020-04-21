package com.example.arenamsk.network.utils

import com.example.arenamsk.network.models.ApiError
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.utils.SharedPreferenceManager

/** Класс с полезными методами для работы с авторизацией */
object AuthUtils {

    /** Manager для работы с SharedPreferences */
    private val manager: SharedPreferenceManager by lazy { SharedPreferenceManager.getInstance() }

    /** Пустой обработчик ошибок, если вджруг их не надо обрабатывать */
    val emptyErrorHandler = object : RequestErrorHandler {
        override suspend fun networkUnavailableError() {
        }

        override suspend fun requestFailedError(error: ApiError?) {
        }

        override suspend fun requestSuccessButResponseIsNull() {
        }

        override suspend fun timeoutException() {
        }
    }

    /** Сохраняем флаг что пользователь авторизирован */
    fun setUserIsAuthorized(isAuthorized: Boolean) {
        manager.saveValue(SharedPreferenceManager.KEY.IS_USER_AUTHORIZED, isAuthorized)
    }

    /** Сохраняем флаг что пользователь пропустил авторизацию */
    fun setUserIsDefault(isDefault: Boolean) {
        manager.saveValue(SharedPreferenceManager.KEY.IS_USER_DEFAULT, isDefault)
    }

    /** Проверка флага авторизирован ли пользователь */
    fun isUserAuthorized(): Boolean = manager.getBooleanValue(
        SharedPreferenceManager.KEY.IS_USER_AUTHORIZED,
        false
    ) as Boolean

    /** Проверка флага пользователь пропускал авторизацию или нет */
    fun isUserDefault(): Boolean = manager.getBooleanValue(
        SharedPreferenceManager.KEY.IS_USER_DEFAULT,
        false
    ) as Boolean

    /** Tokens */

    /** Сохраняем access token */
    fun saveAuthToken(authToken: String) {
        manager.saveValue(SharedPreferenceManager.KEY.AUTH_TOKEN, authToken)
    }

    /** Сохраняем refresh token */
    fun saveRefreshToken(refreshToken: String) {
        manager.saveValue(SharedPreferenceManager.KEY.REFRESH_TOKEN, refreshToken)
    }

    /** Сохраняем timestamp когда access token устареет */
    fun saveExpiredIn(expiredIn: Int) {
        manager.saveValue(SharedPreferenceManager.KEY.EXPIRED_IN, expiredIn)
    }

    /** Получаем access token */
    fun getAuthToken() = manager.getStringValue(SharedPreferenceManager.KEY.AUTH_TOKEN, "") ?: ""

    /** Получаем access token */
    fun getRefreshToken() = manager.getStringValue(SharedPreferenceManager.KEY.REFRESH_TOKEN, "") ?: ""

    /** Получаем timestamp когда access token устареет  */
    fun getExpiredIn() = manager.getIntValue(SharedPreferenceManager.KEY.EXPIRED_IN, 0) ?: 0

}