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

    private var placesLiveData = MutableLiveData<MutableList<PlaceModel>>()
    private var favouritesPlacesLiveData = MutableLiveData<MutableList<PlaceModel>>()
    private var foundedPlacesLiveData = MutableLiveData<MutableList<PlaceModel>>()
    private var foundedFavouritesPlacesLiveData = MutableLiveData<MutableList<PlaceModel>>()
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

    /** Отображаем данные */
    private fun getPlacesSuccess(places: List<PlaceModel>) {
        //Сохраняем все площадки
        placesLiveData.value = places as MutableList<PlaceModel>

        //Сохраняем площадки добавленные в избранное
        favouritesPlacesLiveData.value = places.filter { placeModel ->
            placeModel.isFavourite
        } as MutableList<PlaceModel>
    }
}