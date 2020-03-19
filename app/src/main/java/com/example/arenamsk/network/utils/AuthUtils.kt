package com.example.arenamsk.network.utils

import com.example.arenamsk.network.models.ApiError
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.utils.SharedPreferenceManager

object AuthUtils {

    private val manager: SharedPreferenceManager by lazy { SharedPreferenceManager.getInstance() }

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

    fun setUserIsAuthorized(isAuthorized: Boolean) {
        manager.saveValue(SharedPreferenceManager.KEY.IS_USER_AUTHORIZED, isAuthorized)
    }

    fun setUserIsDefault(isDefault: Boolean) {
        manager.saveValue(SharedPreferenceManager.KEY.IS_USER_DEFAULT, isDefault)
    }

    fun isUserAuthorized(): Boolean = manager.getBooleanValue(
        SharedPreferenceManager.KEY.IS_USER_AUTHORIZED,
        false
    ) as Boolean

    fun isUserDefault(): Boolean = manager.getBooleanValue(
        SharedPreferenceManager.KEY.IS_USER_DEFAULT,
        false
    ) as Boolean

    /**
     * Tokens
     * */
    fun saveAuthToken(authToken: String) {
        manager.saveValue(SharedPreferenceManager.KEY.AUTH_TOKEN, authToken)
    }

    fun saveRefreshToken(refreshToken: String) {
        manager.saveValue(SharedPreferenceManager.KEY.REFRESH_TOKEN, refreshToken)
    }

    fun saveExpiredIn(expiredIn: Int) {
        manager.saveValue(SharedPreferenceManager.KEY.EXPIRED_IN, expiredIn)
    }

    fun getAuthToken() = manager.getStringValue(SharedPreferenceManager.KEY.AUTH_TOKEN, "") ?: ""

    fun getRefreshToken() = manager.getStringValue(SharedPreferenceManager.KEY.REFRESH_TOKEN, "") ?: ""

    fun getExpiredIn() = manager.getIntValue(SharedPreferenceManager.KEY.EXPIRED_IN, 0) ?: 0

}