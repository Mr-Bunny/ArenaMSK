package com.example.arenamsk.ui.places

import androidx.lifecycle.MutableLiveData
import com.example.arenamsk.models.PlaceFilterModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.network.models.ApiError
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.network.utils.AuthUtils.emptyErrorHandler
import com.example.arenamsk.repositories.PlaceRepository
import com.example.arenamsk.room.tables.Subway
import com.example.arenamsk.ui.base.BaseViewModel
import com.example.arenamsk.utils.EnumUtils.GetPlacesStatus
import com.example.arenamsk.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlacesViewModel : BaseViewModel() {

    //Статус запроса площадок
    private val placesStatus = SingleLiveEvent<GetPlacesStatus>()

    //Список всех площадок
    private var placesLiveData = MutableLiveData<MutableList<PlaceModel>>()
    //Список найденных площадок среди загруженных (либо все, либо найденные по фильтру)
    private var foundedPlacesLiveData = MutableLiveData<MutableList<PlaceModel>>()
    //Модель фильтра
    private var filterLiveData = MutableLiveData<PlaceFilterModel>()
    //Список станций метро
    private var subwaysLiveData = MutableLiveData<List<Subway>>()

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
        loadSubways()
    }

    fun getPlacesLiveData() = placesLiveData

    fun getSubwaysLiveData() = subwaysLiveData

    fun getFoundedPlacesLiveData() = foundedPlacesLiveData

    fun getFilterLiveData() = filterLiveData

    fun getPlacesStatusLiveData() = placesStatus

    fun addPlaceToFavourite(
        toFavourite: Boolean,
        place: PlaceModel,
        requestAddToFavouriteFailed: (toFavourite: Boolean, place: PlaceModel) -> Unit
    ) {
        repository.addPlaceToFavourite(toFavourite,
            place.id,
            object : RequestErrorHandler {
                override suspend fun networkUnavailableError() {
                    requestAddToFavouriteFailed(toFavourite, place)
                }

                override suspend fun requestFailedError(error: ApiError?) {
                    requestAddToFavouriteFailed(toFavourite, place)
                }

                override suspend fun requestSuccessButResponseIsNull() {
                    requestAddToFavouriteFailed(toFavourite, place)
                }

                override suspend fun timeoutException() {
                    requestAddToFavouriteFailed(toFavourite, place)
                }
            }
        )
    }

    /** Ищем и отображаем площадки с введенным текстом (это может быть или заголовок или адрес) */
    fun showFilteredPlaces(textToSearch: String) {
        if (textToSearch.isEmpty()) {
            handlePlaces(placesLiveData.value ?: emptyList())
            return
        }

        launch(Dispatchers.IO) {
            val placeList = placesLiveData.value

            val founded = placeList?.filter { place ->
                place.placeTitle.toLowerCase().contains(textToSearch.toLowerCase()) ||
                        place.address.toLowerCase().contains(textToSearch.toLowerCase())
            } as? MutableList<PlaceModel> ?: mutableListOf()

            withContext(Dispatchers.Main) { foundedPlacesLiveData.value = founded }
        }
    }

    /** Ищем новые площадки на основе фильтра */
    fun updatePlaceWithFilter(
        hasBaths: Boolean = false,
        hasInventory: Boolean = false,
        hasLockers: Boolean = false,
        hasParking: Boolean = false,
        openField: Boolean = false,
        priceFrom: Int = 0,
        priceTo: Int = 100000,
        sports: ArrayList<String>,
        subways: Subway
    ) {
        val newFilter = PlaceFilterModel(
            hasBaths,
            hasInventory,
            hasLockers,
            hasParking,
            openField,
            priceFrom,
            priceTo,
            sports,
            subways
        )

        filterLiveData.value = newFilter

        placesStatus.value = GetPlacesStatus.LOAD_PLACES

        repository.getPlaces(
            filter = newFilter,
            success = ::getFilteredPlacesSuccess,
            errorHandler = errorHandler
        )
    }

    //Когда нужно просто подгрузить площадки с текущим фильтром
    fun updatePlaceWithFilter() {
        updatePlaceWithFilter(filterLiveData.value ?: PlaceFilterModel())
    }

    fun updatePlaceWithFilter(newFilter: PlaceFilterModel) {
        filterLiveData.value = newFilter

        placesStatus.value = GetPlacesStatus.LOAD_PLACES

        repository.getPlaces(
            filter = newFilter,
            success = ::getFilteredPlacesSuccess,
            errorHandler = errorHandler
        )
    }

    fun resetFilter() {
        filterLiveData.value = PlaceFilterModel()

        repository.getPlaces(
            filter = filterLiveData.value!!,
            success = ::getFilteredPlacesSuccess,
            errorHandler = errorHandler
        )
    }

    /** Отображаем данные */
    private fun getPlacesSuccess(places: List<PlaceModel>) {
        handlePlaces(places)
    }

    private fun getFilteredPlacesSuccess(places: List<PlaceModel>) {
        handlePlaces(places)
    }

    private fun handlePlaces(places: List<PlaceModel>) {
        //Сохраняем найденные площадки
        placesLiveData.value = places as MutableList<PlaceModel>
    }

    private fun loadSubways() {
        launch(Dispatchers.IO) {
            repository.getSubways(
                success = { subwaysLiveData.value = it },
                errorHandler = emptyErrorHandler
            )
        }
    }
}