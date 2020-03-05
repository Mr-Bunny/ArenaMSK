package com.example.arenamsk.repositories

import com.example.arenamsk.datasources.RemoteDataSource
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
        success: (response: List<PlaceModel>) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = { RemoteDataSource.getPlaces() },
        success = success,
        errorHandler = errorHandler
    )

}