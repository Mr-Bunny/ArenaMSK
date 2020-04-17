package com.example.arenamsk.ui.booked.booked_pager_item

import androidx.lifecycle.MutableLiveData
import com.example.arenamsk.models.OrderModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.network.models.ApiError
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.repositories.PlaceRepository
import com.example.arenamsk.ui.base.BaseViewModel
import com.example.arenamsk.utils.EnumUtils
import com.example.arenamsk.utils.MyLocation
import com.example.arenamsk.utils.SingleLiveEvent
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