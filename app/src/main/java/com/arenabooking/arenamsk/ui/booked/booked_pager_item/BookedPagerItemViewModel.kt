package com.arenabooking.arenamsk.ui.booked.booked_pager_item

import androidx.lifecycle.MutableLiveData
import com.arenabooking.arenamsk.models.OrderModel
import com.arenabooking.arenamsk.models.PlaceModel
import com.arenabooking.arenamsk.network.models.ApiError
import com.arenabooking.arenamsk.network.models.RequestErrorHandler
import com.arenabooking.arenamsk.repositories.PlaceRepository
import com.arenabooking.arenamsk.ui.base.BaseViewModel
import com.arenabooking.arenamsk.utils.EnumUtils
import com.arenabooking.arenamsk.utils.MyLocation
import com.arenabooking.arenamsk.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookedPagerItemViewModel: BaseViewModel() {

    //Статус запроса площадок
    private val placesStatus = SingleLiveEvent<EnumUtils.GetPlacesStatus>()

    private val repository = PlaceRepository.getInstance()

    //Список текущих забронированных площадок
    private var currentBookedPlacesLiveData = MutableLiveData<MutableList<OrderModel>>()
    //Список площадок в истории бронирования
    private var bookedHistoryPlacesLiveData = MutableLiveData<MutableList<PlaceModel>>()

    /** Обработчик ошибок запроса */
    private val errorHandler = object : RequestErrorHandler {
        override suspend fun networkUnavailableError() {
            placesStatus.value = EnumUtils.GetPlacesStatus.NETWORK_OFFLINE
        }

        override suspend fun requestFailedError(error: ApiError?) {
            placesStatus.value = EnumUtils.GetPlacesStatus.REQUEST_ERROR
        }

        override suspend fun requestSuccessButResponseIsNull() {
            placesStatus.value = EnumUtils.GetPlacesStatus.NOT_FOUND
        }

        override suspend fun timeoutException() {
            placesStatus.value = EnumUtils.GetPlacesStatus.REQUEST_ERROR
        }
    }

    fun getPlacesStatusLiveData() = placesStatus

    fun getCurrentBookedPlacesLiveData() = currentBookedPlacesLiveData

    fun getInBookedHistoryPlacesLiveData() = bookedHistoryPlacesLiveData

    fun loadCurrentBookedPlaces() {
        placesStatus.value = EnumUtils.GetPlacesStatus.LOAD_PLACES

        launch(Dispatchers.IO) {
            repository.getCurrentBookedPlaces(
                success = { currentBookedPlacesLiveData.value = it as MutableList<OrderModel> },
                errorHandler = errorHandler
            )
        }
    }

    fun loadInBookedHistoryPlaces() {
        placesStatus.value = EnumUtils.GetPlacesStatus.LOAD_PLACES

        launch(Dispatchers.IO) {
            repository.getBookedHistoryPlaces(
                success = { bookedHistoryPlacesLiveData.value =  MyLocation.getPlacesWithDistance((it.filter { place -> place.inHistory })) as MutableList<PlaceModel>},
                errorHandler = errorHandler
            )
        }
    }
}