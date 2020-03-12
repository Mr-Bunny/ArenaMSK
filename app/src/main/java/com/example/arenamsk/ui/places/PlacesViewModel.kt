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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlacesViewModel : BaseViewModel() {

    //Статус запроса площадок
    private val placesStatus = SingleLiveEvent<GetPlacesStatus>()

    //Список всех площадок
    private var placesLiveData = MutableLiveData<MutableList<PlaceModel>>()
    //Список площадок, которые в избранном, берутся локально из обшего списка найденных площадок
    //При открытии экрана избранных, все площадки подгружаются заново, т.е. фильтр сбрасывается
    private var favouritesPlacesLiveData = MutableLiveData<MutableList<PlaceModel>>()
    //Список найденных площадок среди загруженных (либо все, либо найденные по фильтру)
    private var foundedPlacesLiveData = MutableLiveData<MutableList<PlaceModel>>()
    //Список найденных избранных площадок среди загруженных (либо все, либо найденные по фильтру)
    private var foundedFavouritesPlacesLiveData = MutableLiveData<MutableList<PlaceModel>>()
    //Модель фильтра
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

    fun getFavouritesPlacesLiveData() = favouritesPlacesLiveData

    fun getFoundedFavouritesPlacesLiveData() = foundedFavouritesPlacesLiveData

    fun getFoundedPlacesLiveData() = foundedPlacesLiveData

    fun getFilterLiveData() = filterLiveData

    fun getPlacesStatusLiveData() = placesStatus

    /** Делаем запрос на получение списка всех площадок и СК
     * Или если есть фильтр, то делаем запрос с фильтром */
    fun loadPlaces(withFilter: Boolean = false) {
        placesStatus.value = GetPlacesStatus.LOAD_PLACES

        repository.getPlaces(
            sportList = if (withFilter) filterLiveData.value?.sportList else null,
            success = ::getPlacesSuccess,
            errorHandler = errorHandler
        )
    }

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
            loadPlaces(true)
            return
        }

        launch(Dispatchers.IO) {
            val placeList = placesLiveData.value

            val founded = placeList?.filter { place ->
                place.title.toLowerCase().contains(textToSearch.toLowerCase()) ||
                        place.address.toLowerCase().contains(textToSearch.toLowerCase())
            } as? MutableList<PlaceModel> ?: mutableListOf()

            withContext(Dispatchers.Main) { foundedPlacesLiveData.value = founded }
        }
    }

    /** Ищем и отображаем площадки в избранном с введенным текстом (это может быть или заголовок или адрес) */
    fun showFilteredPlacesInFavourites(textToSearch: String) {
        if (textToSearch.isEmpty()) {
            loadPlaces(false)
            return
        }

        launch(Dispatchers.IO) {
            val favouritesPlaceList = favouritesPlacesLiveData.value

            val founded = favouritesPlaceList?.filter { place ->
                place.title.toLowerCase().contains(textToSearch.toLowerCase()) ||
                        place.address.toLowerCase().contains(textToSearch.toLowerCase())
            } as? MutableList<PlaceModel> ?: mutableListOf()

            withContext(Dispatchers.Main) { foundedFavouritesPlacesLiveData.value = founded }
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
        subways: ArrayList<String>
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

        //Сохраняем площадки добавленные в избранное
        favouritesPlacesLiveData.value = places.filter { placeModel ->
            placeModel.isFavourite
        } as MutableList<PlaceModel>
    }

}