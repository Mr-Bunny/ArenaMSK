package com.arenabooking.arenamsk.network.utils

import com.arenabooking.arenamsk.BuildConfig
import com.arenabooking.arenamsk.network.api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Здесь хранятся статические методы для получения сервиса для обращения к серверу */
object RetrofitFactory {

    fun getAuthApiService(): ApiService = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpProvider.getInstance(true))
        .build()
        .create(ApiService::class.java)

    fun getApiService(): ApiService = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpProvider.getInstance())
        .build()
        .create(ApiService::class.java)
}