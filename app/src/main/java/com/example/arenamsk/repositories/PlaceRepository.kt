package com.example.arenamsk.repositories

import com.example.arenamsk.datasources.RemoteDataSource
import com.example.arenamsk.models.PlaceFilterModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.network.utils.AuthUtils
import com.example.arenamsk.utils.toStringTypedArray

class PlaceRepository private constructor() : BaseRepository() {

    companion object {
        @Volatile
        private var repositoryInstance: PlaceRepository? = null

        fun getInstance() = repositoryInstance ?: synchronized(this) {
            repositoryInstance ?: PlaceRepository()
        }
    }

    /** Загрузка площадок с экрана списка площадок */
    fun getPlaces(
        sportList: ArrayList<String>? = null,
        success: (response: List<PlaceModel>) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            if (sportList.isNullOrEmpty()) {
                RemoteDataSource.getPlaces()
            } else {
                //Приводим массив с видами спорта к виду "Футбол,Баскетбол"
                RemoteDataSource.getPlaces(sportList.toStringTypedArray())
            }
        },
        success = success,
        errorHandler = errorHandler
    )

    /** Загрузка площадок с экрана фильтра */
    fun getPlaces(
        filter: PlaceFilterModel,
        success: (response: List<PlaceModel>) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            RemoteDataSource.getPlaces(filter)
        },
        success = success,
        errorHandler = errorHandler
    )

    fun addPlaceToFavourite(
        toFavourite: Boolean,
        placeId: Int,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            with(RemoteDataSource) {
                if (toFavourite) addPlaceToFavourite(placeId) else removePlaceFromFavourite(placeId)
            }
        },
        success = { },
        errorHandler = errorHandler
    )

}