package com.example.arenamsk.ui.places

import androidx.lifecycle.MutableLiveData
import com.example.arenamsk.models.PlaceFilterModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.network.models.ApiError
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.repositories.PlaceRepository
import com.example.arenamsk.ui.base.BaseViewModel
import com.example.arenamsk.utils.EnumUtils.GetPlacesStatus
import com.example.arenamsk.utils.SingleLiveEvent

class PlacesViewModel : BaseViewModel() {

    //Статус запроса площадок
    private val placesStatus = SingleLiveEvent<GetPlacesStatus>()

    private var placesLiveData = MutableLiveData<MutableList<PlaceModel>>()
    private var filterLiveData = MutableLiveData<PlaceFilterModel>()

    private val repository = PlaceRepository.getInstance()

    /** Обработчик ошибок запроса */
    private val errorHandler = object : RequestErrorHandler {
        override suspend fun networkUnavailableError() {
            placesStatus.value = GetPlacesStatus.NETWORK_OFFLINE
        }

        override suspend fun requestFailedError(error: ApiError?) {
            placesStatus.value = GetPlacesStatus.REQUEST_ERROR
        }

        override suspend fun requestSuccessButResponseIsNull() {
            placesStatus.value = GetPlacesStatus.NOT_FOUND
        }

        override suspend fun timeoutException() {
            placesStatus.value = GetPlacesStatus.REQUEST_ERROR
        }
    }

    init {
        loadPlaces()
    }

    fun getPlacesLiveData() = placesLiveData

    fun getFilterLiveData() = filterLiveData

    fun setFilterLiveData(filter: PlaceFilterModel) {
        filterLiveData.value = filter
    }

    fun getPlacesStatusLiveData() = placesStatus

    /** Делаем запрос на получение списка всех площадок и СК
     * Или если есть фильтр, то делаем запрос с фильтром */
    fun loadPlaces(withFilter: Boolean = false) {
        placesStatus.value = GetPlacesStatus.LOAD_PLACES

        repository.getPlaces(
            filter = if (withFilter) filterLiveData.value else null,
            success = ::getPlacesSuccess,
            errorHandler = errorHandler
        )
    }

    fun addPlaceToFavourite(toFavourite: Boolean,
                            placeId: Int,
                            requestAddToFavouriteFailed: (toFavourite: Boolean) -> Unit) {
        repository.addPlaceToFavourite(toFavourite,
            placeId,
            object : RequestErrorHandler {
                override suspend fun networkUnavailableError() {
                    requestAddToFavouriteFailed(toFavourite)
                }

                override suspend fun requestFailedError(error: ApiError?) {
                    requestAddToFavouriteFailed(toFavourite)
                }

                override suspend fun requestSuccessButResponseIsNull() {
                    requestAddToFavouriteFailed(toFavourite)
                }

                override suspend fun timeoutException() {
                    requestAddToFavouriteFailed(toFavourite)
                }
            }
        )
    }

    /** Отображаем данные */
    private fun getPlacesSuccess(places: List<PlaceModel>) {
        placesLiveData.value = places as MutableList<PlaceModel>
    }
}