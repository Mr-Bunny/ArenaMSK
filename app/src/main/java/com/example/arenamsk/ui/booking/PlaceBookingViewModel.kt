package com.example.arenamsk.ui.booking

import androidx.lifecycle.MutableLiveData
import com.example.arenamsk.models.DateModel
import com.example.arenamsk.network.models.ApiError
import com.example.arenamsk.network.models.BookingDateModel
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.repositories.PlaceRepository
import com.example.arenamsk.ui.base.BaseViewModel
import com.example.arenamsk.utils.TimeUtils
import kotlinx.coroutines.launch

class PlaceBookingViewModel : BaseViewModel() {

    private var placeBookingLiveData = MutableLiveData<MutableList<BookingDateModel>>()

    //Текущая дата в формате yyyy-MM-dd, которую будем передавать на бэк для получения расписания
    private val choosedDateLiveData = MutableLiveData<String>()

    private val currentDate: String = TimeUtils.getCurrentDay()

    private val repository = PlaceRepository.getInstance()

    init {
        choosedDateLiveData.value = currentDate
    }

    fun getPlaceBookingLiveData() = placeBookingLiveData

    fun getCurrentDateLiveData() = choosedDateLiveData

    fun setCurrentDate(dateModel: DateModel) {
        choosedDateLiveData.value = TimeUtils.getDateByDateModel(dateModel)
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
}