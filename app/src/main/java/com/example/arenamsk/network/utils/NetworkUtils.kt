package com.example.arenamsk.network.utils

import android.content.Context
import android.net.ConnectivityManager
import com.example.arenamsk.network.models.ApiError
import okhttp3.ResponseBody
import org.json.JSONObject

object NetworkUtils {
    fun isNetworkAvailable(context: Context): Boolean {
        val conManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = conManager.activeNetworkInfo

        return ni != null && ni.isConnected
    }

    fun getErrorMessageFromErrorBody(errorBody: ResponseBody): String = try {
        val jObj = JSONObject(errorBody.string())
        jObj.getString("Message")
    } catch (ex: Exception) {
        ApiError.DEFAULT_ERROR_MESSAGE
    }

    sealed class Result<out T : Any> {
        data class Success<out T : Any>(val data: T?) : Result<T>()
        data class NetworkException<out T : Any>(val apiError: ApiError?) : Result<T>()
    }

}