package com.example.arenamsk.ui.booking

import androidx.lifecycle.MutableLiveData
import com.example.arenamsk.datasources.LocalDataSource
import com.example.arenamsk.models.DateModel
import com.example.arenamsk.network.models.ApiError
import com.example.arenamsk.network.models.BookingDateModel
import com.example.arenamsk.network.models.BookingPlaceModel
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.repositories.PlaceRepository
import com.example.arenamsk.room.tables.User
import com.example.arenamsk.ui.base.BaseViewModel
import com.example.arenamsk.utils.EnumUtils
import com.example.arenamsk.utils.SingleLiveEvent
import com.example.arenamsk.utils.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaceBookingViewModel : BaseViewModel() {

    private var placeBookingLiveData = MutableLiveData<MutableList<BookingDateModel>>()

    //Текущая дата в формате yyyy-MM-dd, которую будем передавать на бэк для получения расписания
    private val choosedDateLiveData = MutableLiveData<String>()

    //Данные пользователя из локальнойБД
    private val userLiveData = MutableLiveData<User>()

    private val currentDate: String = TimeUtils.getCurrentDay()

    //Статус бронирования
    private val bookingStatus = MutableLiveData<String>()

    private val repository = PlaceRepository.getInstance()

    init {
        choosedDateLiveData.value = currentDate

        getUser()
    }

    fun getUserLiveData() = userLiveData

    fun getBookingStatusLiveData() = bookingStatus

    fun getPlaceBookingLiveData() = placeBookingLiveData

    fun getCurrentDateLiveData() = choosedDateLiveData

    fun setCurrentDate(dateModel: DateModel) {
        choosedDateLiveData.value = TimeUtils.getDateByDateModel(dateModel)
    }

    private fun getUser() {
        launch(Dispatchers.IO) {
            val user = LocalDataSource.getUserData() ?: User()

            withContext(Dispatchers.Main) {
                userLiveData.value = user
            }
        }
    }

    fun setNextDate() {
        choosedDateLiveData.value = TimeUtils.getNextDay(choosedDateLiveData.value ?: currentDate)
    }

    /** Загружаем время доступное для бронирования для определенной площадки */
    fun loadBookingData(playgroundId: Long) {
        launch {
            repository.getBookingTimeList(
                playgroundId = playgroundId,
                date = choosedDateLiveData.value ?: TimeUtils.getCurrentDay(),
                success = {
                    placeBookingLiveData.value = it.bookings as MutableList<BookingDateModel>
                },
                errorHandler = object : RequestErrorHandler {
                    override suspend fun networkUnavailableError() {
                        placeBookingLiveData.value = null
                    }

                    override suspend fun requestFailedError(error: ApiError?) {
                        placeBookingLiveData.value = null
                    }

                    override suspend fun requestSuccessButResponseIsNull() {
                        placeBookingLiveData.value = null
                    }

                    override suspend fun timeoutException() {
                        placeBookingLiveData.value = null
                    }
                }
            )
        }
    }

    /** Запрос на бронирование выбраного времени
     * @param bookingModel - DTO для бронирования на время оплаты */
    fun bookPlace(bookingModel: BookingPlaceModel) {
        launch {
            repository.bookPlace(
                bookingPlaceModel = bookingModel,
                success = {
                    bookingStatus.value = it.paymentUrl
                },
                errorHandler = object : RequestErrorHandler {
                    override suspend fun networkUnavailableError() {
                        bookingStatus.value = ""
                    }

                    override suspend fun requestFailedError(error: ApiError?) {
                        bookingStatus.value = ""
                    }

                    override suspend fun requestSuccessButResponseIsNull() {
                        bookingStatus.value = ""
                    }

                    override suspend fun timeoutException() {
                        bookingStatus.value = ""
                    }
                }
            )
        }
    }
}