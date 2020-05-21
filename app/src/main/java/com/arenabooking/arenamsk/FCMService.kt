package com.arenabooking.arenamsk

import com.arenabooking.arenamsk.network.models.ApiError
import com.arenabooking.arenamsk.network.models.RequestErrorHandler
import com.arenabooking.arenamsk.network.utils.AuthUtils
import com.arenabooking.arenamsk.repositories.AuthRepository
import com.arenabooking.arenamsk.utils.NotificationsUtils
import com.arenabooking.arenamsk.utils.SharedPreferenceManager
import com.arenabooking.arenamsk.utils.SharedPreferenceManager.KEY.FCM_TOKEN
import com.arenabooking.arenamsk.utils.SharedPreferenceManager.KEY.FCM_TOKEN_UPDATED
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {

    companion object {
        /** Отправляем на сервер, если удалось отправить сохраняем флаг, что токен отправлен
        * иначе сохраняем что он не отправился, для повторной отправки  */
        fun sendToken(token: String)  {
            if (AuthUtils.isUserAuthorized() && !AuthUtils.isUserDefault()) {
                AuthRepository.getInstance().sendFCMTokenToServer(
                    token = token,
                    success = {
                        SharedPreferenceManager.getInstance().saveValue(FCM_TOKEN_UPDATED, true)
                    },
                    errorHandler = object: RequestErrorHandler {
                        override suspend fun networkUnavailableError() {
                            SharedPreferenceManager.getInstance().saveValue(FCM_TOKEN_UPDATED, false)
                        }

                        override suspend fun requestFailedError(error: ApiError?) {
                            SharedPreferenceManager.getInstance().saveValue(FCM_TOKEN_UPDATED, false)
                        }

                        override suspend fun requestSuccessButResponseIsNull() {
                            SharedPreferenceManager.getInstance().saveValue(FCM_TOKEN_UPDATED, false)
                        }

                        override suspend fun timeoutException() {
                            SharedPreferenceManager.getInstance().saveValue(FCM_TOKEN_UPDATED, false)
                        }
                    }
                )
            }
        }

        /** Получаем текущий FCM токен */
        fun getCurrentFCMToken(): String {
            var token = SharedPreferenceManager.getInstance().getStringValue(FCM_TOKEN, "") ?: ""

            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }

                    token = task.result?.token ?: ""
                    SharedPreferenceManager.getInstance().saveValue(FCM_TOKEN, token)
                })

            return token
        }
    }

    /** При генерации нового токена - сохраняем его локально и пытаемся отправить на сервер */
    override fun onNewToken(token: String) {
        //Сохраняем локально
        SharedPreferenceManager.getInstance().saveValue(FCM_TOKEN, token)
        //Отправляем на сервер
        sendToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        NotificationsUtils.showNotification(
            data["title"] ?: "Arena",
            data["text"] ?: "У вас забронирована площадка",
            data["hours"]?.toInt() ?: 0)
    }
}