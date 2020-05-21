package com.arenabooking.arenamsk.ui.favourites

import androidx.lifecycle.MutableLiveData
import com.arenabooking.arenamsk.models.PlaceModel
import com.arenabooking.arenamsk.network.models.ApiError
import com.arenabooking.arenamsk.network.models.RequestErrorHandler
import com.arenabooking.arenamsk.network.utils.AuthUtils
import com.arenabooking.arenamsk.repositories.PlaceRepository
import com.arenabooking.arenamsk.ui.base.BaseViewModel
import com.arenabooking.arenamsk.utils.EnumUtils.GetPlacesStatus
import com.arenabooking.arenamsk.utils.MyLocation
import com.arenabooking.arenamsk.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouritesViewModel : BaseViewModel() {

    //Статус запроса площадок
    private val placesStatus = SingleLiveEvent<GetPlacesStatus>()

    //Список площадок, которые в избранном
    private var favouritesPlacesLiveData = MutableLiveData<MutableList<PlaceModel>>()
    //Список найденных избранных площадок среди загруженных (либо все, либо найденные по фильтру)
    private var foundedFavouritesPlacesLiveData = MutableLiveData<MutableList<PlaceModel>>()

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
        getFavourites()
    }

    fun getFavouritesPlacesLiveData() = favouritesPlacesLiveData

    fun getFoundedFavouritesPlacesLiveData() = foundedFavouritesPlacesLiveData

    fun getFavouritesStatusPlacesLiveData() = placesStatus

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

    /** Ищем и отображаем площадки в избранном с введенным текстом (это может быть или заголовок или адрес) */
    fun showFilteredPlacesInFavourites(textToSearch: String) {
        if (textToSearch.isEmpty()) {
            getPlacesSuccess(favouritesPlacesLiveData.value ?: emptyList())
            return
        }

        launch(Dispatchers.IO) {
            val favouritesPlaceList = favouritesPlacesLiveData.value

            val founded = favouritesPlaceList?.filter { place ->
                place.placeTitle.toLowerCase().contains(textToSearch.toLowerCase()) ||
                        place.address.toLowerCase().contains(textToSearch.toLowerCase())
            } as? MutableList<PlaceModel> ?: mutableListOf()

            withContext(Dispatchers.Main) { foundedFavouritesPlacesLiveData.value = founded }
        }
    }

    /** Делаем запрос на получение списка всех площадок и СК
     * Или если есть фильтр, то делаем запрос с фильтром */
    fun getFavourites() {
        if (AuthUtils.isUserDefault()) {
            placesStatus.value = GetPlacesStatus.NOT_FOUND
        } else {
            placesStatus.value = GetPlacesStatus.LOAD_PLACES

            repository.getFavourites(
                success = ::getPlacesSuccess,
                errorHandler = errorHandler
            )
        }
    }

    /** Отображаем данные */
    private fun getPlacesSuccess(places: List<PlaceModel>) {
        //Сохраняем площадки добавленные в избранное
        favouritesPlacesLiveData.value = MyLocation.getPlacesWithDistance(places) as MutableList<PlaceModel>
    }
}