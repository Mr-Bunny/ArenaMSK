package com.example.arenamsk.datasources

import android.graphics.Bitmap
import com.example.arenamsk.models.FeedbackModel
import com.example.arenamsk.models.PlaceFilterModel
import com.example.arenamsk.network.api.ApiService
import com.example.arenamsk.network.models.AppFeedbackModel
import com.example.arenamsk.network.models.auth.LogInUserModel
import com.example.arenamsk.network.models.auth.RefreshTokenModel
import com.example.arenamsk.network.models.auth.ResetPasswordModel
import com.example.arenamsk.network.models.auth.SignUpUserModel
import com.example.arenamsk.network.utils.AuthUtils
import com.example.arenamsk.network.utils.RetrofitFactory
import com.example.arenamsk.ui.place_filter.PlaceFilterFragment
import com.example.arenamsk.utils.ImageUtils
import com.example.arenamsk.utils.toStringTypedArray
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

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
    //Если пользователь дефолтный, то запрос площадок без токена
    suspend fun getPlaces() = if (AuthUtils.isUserDefault()) {
        authService.getPLaces()
    } else {
        service.getPLaces()
    }

    //Получаем список избранных площадок
    suspend fun getFavourites() = service.getFavourites()

    //Если пользователь дефолтный, то запрос площадок без токена
    suspend fun getPlaces(sports: String?) = if (AuthUtils.isUserDefault()) {
        authService.getPLaces(sports)
    } else {
        service.getPLaces(sports)
    }

    suspend fun getPlaces(filter: PlaceFilterModel) = with(filter) {
        val apiService: ApiService = if (AuthUtils.isUserDefault()) authService else service

        apiService.getPLaces(
            hasBaths = hasBaths,
            hasInventory = hasInventory,
            hasLockers = hasLockers,
            hasParking = hasParking,
            openField = openField,
            priceFrom = priceFrom,
            priceTo = priceTo,
            sports = sportList?.toStringTypedArray(),
            subways = if (subways == null ||
                subways?.id == null ||
                subways?.id == PlaceFilterFragment.DEFAULT_SUBWAY_ID
            ) {
                null
            } else {
                subways?.id.toString()
            }
        )
    }

    suspend fun addPlaceToFavourite(placeId: Int) = service.addPlaceToFavourite(placeId.toString())

    suspend fun removePlaceFromFavourite(placeId: Int) =
        service.addRemoveFromFavourite(placeId.toString())

    suspend fun getSubways() = authService.getAllSubways()

    suspend fun getCurrentOrders() = service.getCurrentOrders()

    suspend fun getFeedbackList(placeId: String) = authService.getFeedbackList(placeId)

    suspend fun getBookingTimeList(playgroundId: String, date: String) = authService.getBookingTimeList(playgroundId, date)

    suspend fun sendAppFeedback(feedback: AppFeedbackModel) = authService.postAppFeedback(feedback)

    suspend fun sendPlaceFeedback(placeId: String, feedback: FeedbackModel) = service.postPlaceFeedback(placeId, feedback)

    suspend fun updateUserData(name: String) = service.postUserAccountInfo(LocalDataSource.getUserData()!!.copy(firstName = name))

    suspend fun sendEmailToResetPassword(email: String) = service.postUserAccountInfo(ResetPasswordModel(email))

}