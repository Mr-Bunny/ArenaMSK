package com.example.arenamsk.network.api

import com.example.arenamsk.models.FeedbackModel
import com.example.arenamsk.models.OrderModel
import com.example.arenamsk.models.PlaceBookingModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.network.models.*
import com.example.arenamsk.network.models.auth.ResetPasswordModel
import com.example.arenamsk.network.models.auth.*
import com.example.arenamsk.room.tables.Subway
import com.example.arenamsk.room.tables.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    /** POST Запрос на обновление токенов */
    @POST("api/v1/auth/refreshToken/")
    suspend fun updateTokens(@Body refreshToken: RefreshTokenModel): Response<UpdatedTokensModel>

    /** POST Запрос регистрации */
    @POST("api/v1/auth/sign-up/")
    suspend fun postSignUpRequest(@Body signUpModel: SignUpUserModel): Response<User>

    /** POST Запрос загрузки аватарки пользователя */
    @Multipart
    @POST("api/v1/account/upload/avatar/")
    suspend fun uploadAvatar(@Part file: MultipartBody.Part): Response<UserImageUrl>

    /** POST Запрос аторизации */
    @POST("api/v1/auth/sign-in/")
    suspend fun postLogInRequest(@Body logInModel: LogInUserModel): Response<UpdatedTokensModel>

    /** POST Запрос смены пароля на новый */
    @POST("api/v1/account/change-password")
    suspend fun changePassword(@Body passwordChangeModel: PasswordChangeDto): Response<Unit>

    /** GET Запрос на получение информации о пользователе */
    @GET("api/v1/account/")
    suspend fun getUserAccountInfo(): Response<User>

    /** POST Запрос загрузки информации о пользователе на сервер */
    @POST("api/v1/account/")
    suspend fun postUserAccountInfo(@Body user: User): Response<User>

    /** DELETE Запрос удаления пользователя */
    @DELETE("api/v1/account/")
    suspend fun deleteAccount(): Response<Unit>

    /** POST Запрос просьбы о смене пароля */
    @POST("api/v1/account/reset-password/init")
    suspend fun postUserAccountInfo(@Body resetPasswordModel: ResetPasswordModel): Response<Unit>

    /** GET Запрос получения отзывов о площадке */
    @GET("api/v1/feedback/{place}")
    suspend fun getFeedbackList(@Path("place") place: String): Response<FeedbackNetworkModel>

    /** GET Запрос на получение расписания площадки на выбранную дату */
    @GET("api/v1/booking/{playground}")
    suspend fun getBookingTimeList(@Path("playground") playground: String, @Query("date") date: String): Response<PlaceBookingModel>

    /** GET Запрос списка станций метро */
    @GET("api/v1/subway/")
    suspend fun getAllSubways(): Response<List<Subway>>

    /** GET Запрос на получение всех площадок */
    @GET("api/v1/place/")
    suspend fun getPLaces(): Response<List<PlaceModel>>

    /** GET Запрос на получение избранных площадок */
    @GET("api/v1/place/favorite")
    suspend fun getFavourites(): Response<List<PlaceModel>>

    /** GET Запрос на получение площадок с определенным видом спорта */
    @GET("api/v1/place/")
    suspend fun getPLaces(@Query("sports", encoded = true) sports: String?): Response<List<PlaceModel>>

    /** GET Запрос на получение списка площадок на основе фильтра */
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

    /** POST Запрос добавления площадки в избранное */
    @POST("api/v1/favorite/mark/{place}")
    suspend fun addPlaceToFavourite(@Path("place") place: String): Response<Unit>

    /** POST Запрос удаления площадки из избранного */
    @POST("api/v1/favorite/unmark/{place}")
    suspend fun addRemoveFromFavourite(@Path("place") place: String): Response<Unit>

    /** POST Запрос отправки отзыва о площадке */
    @POST("api/v1/feedback/service")
    suspend fun postAppFeedback(@Body feedback: AppFeedbackModel): Response<Unit>

    /** POST Запрос бронирования площадки на определенное время и дату, успешный запрос возвращает ссылку на оплату*/
    @POST("api/v1/booking/booking")
    suspend fun postBookingModel(@Body bookingPlaceModel: BookingPlaceModel): Response<BookingResponseModel>

    /** POST Запрос на отправку отзыва о площадке */
    @POST("api/v1/feedback/{place}")
    suspend fun postPlaceFeedback(@Path("place") place: String, @Body feedback: FeedbackModel): Response<Unit>

    /** GET Запрос получения информации о текущих забронированных площадках */
    @GET("api/v1/booking/booked")
    suspend fun getCurrentOrders(): Response<List<OrderModel>>

    /** POST Запрос на отправку firebase токена для получения уведомлений */
    @POST("api/v1/account/device/token/")
    suspend fun postFCMToken(@Body token: FCMTokenModel): Response<Unit>
}