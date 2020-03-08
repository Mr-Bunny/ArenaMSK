package com.example.arenamsk.repositories

import com.example.arenamsk.datasources.RemoteDataSource
import com.example.arenamsk.models.PlaceFilterModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.network.models.RequestErrorHandler

class PlaceRepository private constructor() : BaseRepository() {

    companion object {
        @Volatile
        private var repositoryInstance: PlaceRepository? = null

        fun getInstance() = repositoryInstance ?: synchronized(this) {
            repositoryInstance ?: PlaceRepository()
        }
    }

    fun getPlaces(
        filter: PlaceFilterModel? = null,
        success: (response: List<PlaceModel>) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            if (filter?.sportList.isNullOrEmpty()) {
                RemoteDataSource.getPlaces()
            } else {
                //Приводим массив с видами спорта к виду "Футбол,Баскетбол"
                val typedArray = filter?.sportList?.toString()
                    ?.replace(" ", "")
                    ?.replace("[", "")
                    ?.replace("]", "")
                RemoteDataSource.getPlaces(typedArray)
            }
        },
        success = success,
        errorHandler = errorHandler
    )

}