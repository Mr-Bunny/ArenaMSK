package com.arenabooking.arenamsk.datasources

import com.arenabooking.arenamsk.models.FeedbackModel
import com.arenabooking.arenamsk.models.PlaceFilterModel
import com.arenabooking.arenamsk.network.api.ApiService
import com.arenabooking.arenamsk.network.models.AppFeedbackModel
import com.arenabooking.arenamsk.network.models.BookingPlaceModel
import com.arenabooking.arenamsk.network.models.FCMTokenModel
import com.arenabooking.arenamsk.network.models.auth.*
import com.arenabooking.arenamsk.network.utils.AuthUtils
import com.arenabooking.arenamsk.network.utils.RetrofitFactory
import com.arenabooking.arenamsk.ui.place_filter.PlaceFilterFragment
import com.arenabooking.arenamsk.utils.toStringTypedArray
import okhttp3.MultipartBody

/** DataSource отвечающий за работу с данными, которые мы получаем с сервера */
object RemoteDataSource {
    /** Если пользователь авторизирован, используем этот сервис, тут подставляются токены в заголовк запроса */
    private val service: ApiService
        get() = RetrofitFactory.getApiService()

    /** Сервис для использования, если пользователь не атворизирован, токен не используется */
    private val authService: ApiService = RetrofitFactory.getAuthApiService()

    /** Auth */

    /** Делаем запрос на обновление токенов */
    suspend fun updateToken() = service.updateTokens(RefreshTokenModel(AuthUtils.getRefreshToken()))

    /** Делаем запрос на отправку модели пользователя на сервер */
    suspend fun startSignUp(model: SignUpUserModel) = authService.postSignUpRequest(model)

    /** Загружаем аватар на сервер */
    suspend fun uploadAvatar(image: MultipartBody.Part) = service.uploadAvatar(image)

    /** Запрос авторизации пользователя */
    suspend fun startLogIn(model: LogInUserModel) = authService.postLogInRequest(model)

    /** Получение информации о пользователе */
    suspend fun getAccountInfo() = service.getUserAccountInfo()

    /** Сохраняем новый пароль */
    suspend fun changePassword(passwordChangeModel: PasswordChangeDto) =
        service.changePassword(passwordChangeModel)

    /** Places */
    /** Если пользователь дефолтный, то делаем запрос площадок без токена */
    suspend fun getPlaces() = if (AuthUtils.isUserDefault()) {
        authService.getPLaces()
    } else {
        service.getPLaces()
    }

    /** Получаем список избранных площадок */
    suspend fun getFavourites() = service.getFavourites()

    /** Если пользователь дефолтный, то запрос площадок с выбранным видом спорта без токена */
    suspend fun getPlaces(sports: String?) = if (AuthUtils.isUserDefault()) {
        authService.getPLaces(sports)
    } else {
        service.getPLaces(sports)
    }

    /** Делаем запрос площадок на основе фильтра */
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

    /** Запрос добавления площадки в избранное
     * @param placeId - Id площадки (PlaceModel) */
    suspend fun addPlaceToFavourite(placeId: Int) = service.addPlaceToFavourite(placeId.toString())

    /** Запрос удаления площадки из избранного
     * @param placeId - Id площадки (PlaceModel) */
    suspend fun removePlaceFromFavourite(placeId: Int) =
        service.addRemoveFromFavourite(placeId.toString())

    /** Запрос на получения списка станций метро */
    suspend fun getSubways() = authService.getAllSubways()

    /** Получаем список с инфомрацией о текущих забронированных площадках */
    suspend fun getCurrentOrders() = service.getCurrentOrders()

    /** Получаем список отзывов о площадке
     * @param placeId - Id площадки (PlaceModel)  */
    suspend fun getFeedbackList(placeId: String) = authService.getFeedbackList(placeId)

    /** Получаем расписание площадки за определенную дату
     * @param placeId - Id площадки (PlaceModel)
     * @param date - Выбранная дата в формате yyyy-MM-dd */
    suspend fun getBookingTimeList(playgroundId: String, date: String) =
        authService.getBookingTimeList(playgroundId, date)

    /** Отправка отзыва о приложении
     * @param feedback - DTO с отзывом для отправки на сервер */
    suspend fun sendAppFeedback(feedback: AppFeedbackModel) = authService.postAppFeedback(feedback)

    /** Делаем запрос на бронирование определенного времени
     * @param bookingPlaceModel - DTO модель с информацией о брони */
    suspend fun postBookingModel(bookingPlaceModel: BookingPlaceModel) =
        if (AuthUtils.isUserDefault())
            authService.postBookingModel(bookingPlaceModel)
        else
            service.postBookingModel(bookingPlaceModel)

    /** Отправка отзыва о площадке
     * @param placeId - Id площадки
     * @param feedback - DTO с отзывом */
    suspend fun sendPlaceFeedback(placeId: String, feedback: FeedbackModel) =
        service.postPlaceFeedback(placeId, feedback)

    /** Обновление имени пользователя, которое было изменено на экране редактирования профиля
     * @param name - Новое имя, мы копируем объект текущего пользователя из локальной БД, сохраняем в него новое имя и
     * сохраняем на сервер */
    suspend fun updateUserData(name: String) =
        service.postUserAccountInfo(LocalDataSource.getUserData()!!.copy(firstName = name))

    /** Отправляем запрос на смену пароля
     * @param resetModel - DTO с информацией для подтверждения смены пароля */
    suspend fun sendEmailToResetPassword(resetModel: ResetPasswordModel) =
        service.postUserAccountInfo(resetModel)

    /** Запрос на удаление аккаунта */
    suspend fun deleteAccount() = service.deleteAccount()

    /** Запрос на сохранение firebase токена для получения уведомлений */
    suspend fun postFCMToken(token: FCMTokenModel) = service.postFCMToken(token)

}