package com.example.arenamsk.datasources

import com.example.arenamsk.network.api.ApiService
import com.example.arenamsk.network.models.auth.LogInUserModel
import com.example.arenamsk.network.models.auth.RefreshTokenModel
import com.example.arenamsk.network.models.auth.SignUpUserModel
import com.example.arenamsk.network.utils.AuthUtils
import com.example.arenamsk.network.utils.RetrofitFactory
import okhttp3.MultipartBody

object RemoteDataSource {
    private val service: ApiService
        get() = RetrofitFactory.getApiService()

    private val authService: ApiService = RetrofitFactory.getAuthApiService()

    /** Auth */
    suspend fun updateToken() = service.updateTokens(RefreshTokenModel(AuthUtils.getRefreshToken()))

    suspend fun startSignUp(model: SignUpUserModel) = authService.postSignUpRequest(model)

    suspend fun uploadAvatar(image: MultipartBody.Part) = service.uploadAvatar(image)

    suspend fun startLogIn(model: LogInUserModel) = authService.postLogInRequest(model)

    suspend fun getAccountInfo() = service.getUserAccountInfo()

    /** Places */
    suspend fun getPlaces() = service.getPLaces()

    suspend fun getPlaces(sports: String?) = service.getPLaces(sports)

    suspend fun addPlaceToFavourite(placeId: Int) = service.addPlaceToFavourite(placeId.toString())

    suspend fun removePlaceFromFavourite(placeId: Int) = service.addRemoveFromFavourite(placeId.toString())

}