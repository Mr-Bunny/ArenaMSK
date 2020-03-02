package com.example.arenamsk.ui.places

import androidx.lifecycle.MutableLiveData
import com.example.arenamsk.models.FeedbackModel
import com.example.arenamsk.models.PlaceFilterModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.network.models.ApiError
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.repositories.PlaceRepository
import com.example.arenamsk.ui.base.BaseViewModel

class PlacesViewModel : BaseViewModel() {

    private var placesLiveData = MutableLiveData<MutableList<PlaceModel>>()

    private var filterLiveData = MutableLiveData<PlaceFilterModel>()

    private val repository = PlaceRepository.getInstance()

    /** Обработчик ошибок запроса */
    private val errorHandler = object : RequestErrorHandler {
        override suspend fun networkUnavailableError() {

        }

        override suspend fun requestFailedError(error: ApiError?) {

        }

        override suspend fun requestSuccessButResponseIsNull() {

        }

        override suspend fun timeoutException() {

        }
    }

    init {
        loadPlaces()
    }

    private fun loadPlaces() {
        repository.getPlaces(
            success = ::getPlacesSuccess,
            errorHandler = errorHandler
        )

        //TODO при изменении фильтра, делать новый запрос, отменяя старый с учетом фильтров
    }

    fun getPlacesLiveData() = placesLiveData

    fun getFilterLiveData() = filterLiveData

    //Отображаем данные во view
    private fun getPlacesSuccess(places: List<PlaceModel>) {
        placesLiveData.value = places as MutableList<PlaceModel>
    }

}