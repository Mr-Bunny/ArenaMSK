package com.example.arenamsk.network.utils

import android.content.Context
import android.net.ConnectivityManager
import com.example.arenamsk.network.models.ApiError
import okhttp3.ResponseBody
import org.json.JSONObject

/** Класс с полезными методами для работы с сетью */
object NetworkUtils {

    /** Проверка доступа к интернету */
    fun isNetworkAvailable(context: Context): Boolean {
        val conManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = conManager.activeNetworkInfo

        return ni != null && ni.isConnected
    }

    /** Получаем ошибку из тела ответа на запрос */
    fun getErrorMessageFromErrorBody(errorBody: ResponseBody): String = try {
        val jObj = JSONObject(errorBody.string())
        jObj.getString("message")
    } catch (ex: Exception) {
        ApiError.DEFAULT_ERROR_MESSAGE
    }

    /** Класс представляющий собой результат запроса */
    sealed class Result<out T : Any> {
        //Запрос прошел успешно
        data class Success<out T : Any>(val data: T?) : Result<T>()
        //Запрос вернул ошибку
        data class NetworkException<out T : Any>(val apiError: ApiError?) : Result<T>()
    }

}