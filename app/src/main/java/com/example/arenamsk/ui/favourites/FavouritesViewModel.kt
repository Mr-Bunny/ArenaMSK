package com.example.arenamsk.ui.favourites

import androidx.lifecycle.MutableLiveData
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.network.models.ApiError
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.repositories.PlaceRepository
import com.example.arenamsk.ui.base.BaseViewModel

class FavouritesViewModel : BaseViewModel() {

    private var favouritesPlacesLiveData = MutableLiveData<MutableList<PlaceModel>>()

    private val repository = PlaceRepository.getInstance()

    fun loadFavouritesPlaces() {
        //favouritesPlacesLiveData.value = getTestPlaces()
    }

    fun getFavouritesPlacesLiveData() = favouritesPlacesLiveData


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
}