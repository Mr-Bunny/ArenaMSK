package com.example.arenamsk.ui.booking

import androidx.lifecycle.MutableLiveData
import com.example.arenamsk.models.DateModel
import com.example.arenamsk.models.PlaceBookingModel
import com.example.arenamsk.ui.base.BaseViewModel
import com.example.arenamsk.utils.TimeUtils

class PlaceBookingViewModel : BaseViewModel() {

    private var placeBookingLiveData = MutableLiveData<MutableList<PlaceBookingModel>>()

    //Текущая дата в формате yyyy-MM-dd, которую будем передавать на бэк для получения расписания
    private val choosedDateLiveData = MutableLiveData<String>()

    private val currentDate: String = TimeUtils.getCurrentDay()

    init {
        choosedDateLiveData.value = currentDate

        loadBookingData()
    }

    fun getPlaceBookingLiveData() = placeBookingLiveData

    fun getCurrentDateLiveData() = choosedDateLiveData

    fun setCurrentDate(dateModel: DateModel) {
        choosedDateLiveData.value = TimeUtils.getDateByDateModel(dateModel)
    }

    fun setNextDate() {
        choosedDateLiveData.value = TimeUtils.getNextDay(choosedDateLiveData.value ?: currentDate)
    }

    private fun loadBookingData() {
        placeBookingLiveData.value = getTestPlaces()
    }

    //TODO test - remove after get real data from server
    private fun getTestPlaces(): MutableList<PlaceBookingModel> = mutableListOf(
        PlaceBookingModel(
            time = "10.30 - 11.30",
            price = 1000f,
            statusIsFree = true
        ),
        PlaceBookingModel(
            time = "12.30 - 13.30",
            price = 2000f,
            statusIsFree = false
        ),
        PlaceBookingModel(
            time = "13.30 - 14.30",
            price = 3000f,
            statusIsFree = false
        ),
        PlaceBookingModel(
            time = "14.30 - 15.30",
            price = 4000f,
            statusIsFree = true
        ),
        PlaceBookingModel(
            time = "16.30 - 17.30",
            price = 5000f,
            statusIsFree = true
        ),
        PlaceBookingModel(
            time = "10.30 - 11.30",
            price = 1000f,
            statusIsFree = true
        ),
        PlaceBookingModel(
            time = "12.30 - 13.30",
            price = 2000f,
            statusIsFree = false
        ),
        PlaceBookingModel(
            time = "13.30 - 14.30",
            price = 3000f,
            statusIsFree = false
        ),
        PlaceBookingModel(
            time = "14.30 - 15.30",
            price = 4000f,
            statusIsFree = true
        ),
        PlaceBookingModel(
            time = "16.30 - 17.30",
            price = 5000f,
            statusIsFree = true
        ),
        PlaceBookingModel(
            time = "14.30 - 15.30",
            price = 4000f,
            statusIsFree = true
        ),
        PlaceBookingModel(
            time = "16.30 - 17.30",
            price = 5000f,
            statusIsFree = true
        ),
        PlaceBookingModel(
            time = "10.30 - 11.30",
            price = 1000f,
            statusIsFree = true
        ),
        PlaceBookingModel(
            time = "12.30 - 13.30",
            price = 2000f,
            statusIsFree = false
        ),
        PlaceBookingModel(
            time = "13.30 - 14.30",
            price = 3000f,
            statusIsFree = false
        ),
        PlaceBookingModel(
            time = "14.30 - 15.30",
            price = 4000f,
            statusIsFree = true
        ),
        PlaceBookingModel(
            time = "16.30 - 17.30",
            price = 5000f,
            statusIsFree = true
        )
    )
}