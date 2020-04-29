package com.example.arenamsk.repositories

import com.example.arenamsk.datasources.LocalDataSource
import com.example.arenamsk.datasources.RemoteDataSource
import com.example.arenamsk.models.*
import com.example.arenamsk.network.models.*
import com.example.arenamsk.network.models.auth.ResetPasswordModel
import com.example.arenamsk.room.tables.Subway
import com.example.arenamsk.room.tables.User
import com.example.arenamsk.utils.toStringTypedArray

/** Репозиторий связанный с площадками */
class PlaceRepository private constructor() : BaseRepository() {

    companion object {
        @Volatile
        private var repositoryInstance: PlaceRepository? = null

        fun getInstance() = repositoryInstance ?: synchronized(this) {
            repositoryInstance ?: PlaceRepository()
        }
    }

    /** Загрузка избранных площадок
     * @param success - колбэк, который будет вызван в случае успешного запроса, принимает в качестве параметра
     * результат запроса
     * @param errorHandler - Обработчик ошибок, соответствующие
     * методы которые будут вызваны в зависимости от ошибки запроса */
    fun getFavourites(
        success: (response: List<PlaceModel>) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            RemoteDataSource.getFavourites()
        },
        success = success,
        errorHandler = errorHandler
    )

    /** Загрузка площадок с экрана списка площадок */
    fun getPlaces(
        sportList: ArrayList<String>? = null,
        success: (response: List<PlaceModel>) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            if (sportList.isNullOrEmpty()) {
                RemoteDataSource.getPlaces()
            } else {
                //Приводим массив с видами спорта к виду "Футбол,Баскетбол"
                RemoteDataSource.getPlaces(sportList.toStringTypedArray())
            }
        },
        success = success,
        errorHandler = errorHandler
    )

    /** Загрузка площадок с экрана фильтра */
    fun getPlaces(
        filter: PlaceFilterModel,
        success: (response: List<PlaceModel>) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            RemoteDataSource.getPlaces(filter)
        },
        success = success,
        errorHandler = errorHandler
    )

    /** Загрузка текущих забронированных площадок */
    fun getCurrentBookedPlaces(
        success: (response: List<OrderModel>) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            RemoteDataSource.getCurrentOrders()
        },
        success = success,
        errorHandler = errorHandler
    )

    /** Загрузка площадок в истории бронирования */
    fun getBookedHistoryPlaces(
        success: (response: List<PlaceModel>) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            RemoteDataSource.getPlaces()
        },
        success = success,
        errorHandler = errorHandler
    )

    /** Добавление площадки в избранное */
    fun addPlaceToFavourite(
        toFavourite: Boolean,
        placeId: Int,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            with(RemoteDataSource) {
                if (toFavourite) addPlaceToFavourite(placeId) else removePlaceFromFavourite(placeId)
            }
        },
        success = { },
        errorHandler = errorHandler
    )

    /** Загрузка времен бронирования площадки */
    suspend fun getBookingTimeList(
        playgroundId: Long,
        date: String,
        success: (response: PlaceBookingModel) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            RemoteDataSource.getBookingTimeList(playgroundId.toString(), date)
        },
        success = success,
        errorHandler = errorHandler
    )

    /** Загрузка списка станций метро */
    suspend fun getSubways(
        success: (response: List<Subway>) -> Unit,
        errorHandler: RequestErrorHandler
    ) {
        LocalDataSource.getLocalSubwayList().also {
            if (it.isNullOrEmpty()) {
                makeRequest(
                    call = { RemoteDataSource.getSubways() },
                    success = success,
                    errorHandler = errorHandler
                )
            } else {
                success.invoke(it)
            }
        }
    }

    /** Загрузка списка отзывов о площадке */
    suspend fun getFeedbackList(
        placeId: String,
        success: (feedback: FeedbackNetworkModel) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            RemoteDataSource.getFeedbackList(placeId)
        },
        success = success,
        errorHandler = errorHandler
    )

    /** Отправляем отзыв о приложении */
    fun sendAppFeedback(
        feedback: AppFeedbackModel,
        success: (Unit) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            RemoteDataSource.sendAppFeedback(feedback)
        },
        success = success,
        errorHandler = errorHandler
    )

    /** Бронируем площадку по времени */
    fun bookPlace(
        bookingPlaceModel: BookingPlaceModel,
        success: (BookingResponseModel) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            RemoteDataSource.postBookingModel(bookingPlaceModel)
        },
        success = success,
        errorHandler = errorHandler
    )

    /** Отправляем отзыв о площадке */
    fun sendPlaceFeedback(
        placeId: String,
        feedback: FeedbackModel,
        success: (Unit) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            RemoteDataSource.sendPlaceFeedback(placeId, feedback)
        },
        success = success,
        errorHandler = errorHandler
    )

    /** Отправляем отзыв о площадке */
    fun updateUserData(
        name: String,
        success: (User) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            RemoteDataSource.updateUserData(name)
        },
        success = success,
        errorHandler = errorHandler
    )

    /** Отправляем email или телефон, на который должен прийти новый пароль */
    fun sendEmailToResetPassword(
        resetModel: ResetPasswordModel,
        success: (Unit) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            RemoteDataSource.sendEmailToResetPassword(resetModel)
        },
        success = success,
        errorHandler = errorHandler
    )

    /** ЗАпрос на удаление аккаунта */
    fun deleteAccount(
        success: (Unit) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            RemoteDataSource.deleteAccount()
        },
        success = success,
        errorHandler = errorHandler
    )
}