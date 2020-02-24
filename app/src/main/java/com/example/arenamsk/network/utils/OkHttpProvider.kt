package com.example.arenamsk.network.utils

import com.example.arenamsk.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object OkHttpProvider {
    fun getInstance(isAuth: Boolean = false) = provideClient(isAuth)

    private fun provideClient(isAuth: Boolean): OkHttpClient {
        val builder = OkHttpClient.Builder()

        //Interceptor for logs
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
        }

        if (!isAuth) {
            val headerInterceptor = Interceptor {
                var request = it.request()
                val headers = request
                    .headers()
                    .newBuilder()
                    .add("Authorization", "Bearer ${AuthUtils.getAuthToken()}")
                    .build()
                request = request.newBuilder().headers(headers).build()
                it.proceed(request)
            }
            builder.addInterceptor(headerInterceptor)
        }

        return builder.build()
    }
}