package com.example.arenamsk.network.utils

import com.example.arenamsk.BuildConfig
import com.example.arenamsk.network.api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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