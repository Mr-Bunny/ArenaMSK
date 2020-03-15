package com.example.arenamsk.network.api

import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.network.models.auth.*
import com.example.arenamsk.room.tables.Subway
import com.example.arenamsk.room.tables.User
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/v1/auth/refreshToken/")
    suspend fun updateTokens(@Body refreshToken: RefreshTokenModel): Response<UpdatedTokensModel>

    @POST("api/v1/auth/sign-up/")
    suspend fun postSignUpRequest(@Body signUpModel: SignUpUserModel): Response<User>

    @Multipart
    @POST("api/v1/account/upload/avatar/")
    suspend fun uploadAvatar(@Part file: MultipartBody.Part): Response<UserImageUrl>

    @POST("api/v1/auth/sign-in/")
    suspend fun postLogInRequest(@Body logInModel: LogInUserModel): Response<UpdatedTokensModel>

    @GET("api/v1/account/")
    suspend fun getUserAccountInfo(): Response<User>

    @GET("api/v1/subway/")
    suspend fun getAllSubways(): Response<List<Subway>>

    @GET("api/v1/place/")
    suspend fun getPLaces(): Response<List<PlaceModel>>

    @GET("api/v1/place/favorite")
    suspend fun getFavourites(): Response<List<PlaceModel>>

    @GET("api/v1/place/")
    suspend fun getPLaces(@Query("sports", encoded = true) sports: String?): Response<List<PlaceModel>>

    @GET("api/v1/place/")
    suspend fun getPLaces(@Query("hasBaths") hasBaths: Boolean,
                          @Query("hasInventory") hasInventory: Boolean,
                          @Query("hasLockers") hasLockers: Boolean,
                          @Query("hasParking") hasParking: Boolean,
                          @Query("openField") openField: Boolean,
                          @Query("priceFrom") priceFrom: Int,
                          @Query("priceTo") priceTo: Int,
                          @Query("sports", encoded = true) sports: String?,
                          @Query("subways", encoded = true) subways: String?): Response<List<PlaceModel>>

    @POST("api/v1/favorite/mark/{place}")
    suspend fun addPlaceToFavourite(@Path("place") place: String): Response<Unit>

    @POST("api/v1/favorite/unmark/{place}")
    suspend fun addRemoveFromFavourite(@Path("place") place: String): Response<Unit>
}