package com.example.arenamsk.network.api

import com.example.arenamsk.network.models.auth.LogInUserModel
import com.example.arenamsk.network.models.auth.RefreshTokenModel
import com.example.arenamsk.network.models.auth.SignUpUserModel
import com.example.arenamsk.network.models.auth.UpdatedTokensModel
import com.example.arenamsk.room.tables.User
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/v1/auth/refreshToken/")
    suspend fun updateTokens(@Body refreshToken: RefreshTokenModel): Response<UpdatedTokensModel>

    @POST("api/v1/auth/sign-up/")
    suspend fun postSignUpRequest(@Body signUpModel: SignUpUserModel): Response<User>

    @Multipart
    @POST("api/v1/account/upload/avatar/")
    suspend fun uploadAvatar(@Part file: MultipartBody.Part): Response<String>

    @POST("api/v1/auth/sign-in/")
    suspend fun postLogInRequest(@Body logInModel: LogInUserModel): Response<UpdatedTokensModel>

    @GET("api/v1/account/")
    suspend fun getUserAccountInfo(): Response<User>

    @GET("closed")
    suspend fun testClosed(): Response<JsonObject>

}