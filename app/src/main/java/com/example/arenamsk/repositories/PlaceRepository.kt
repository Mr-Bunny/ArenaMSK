package com.example.arenamsk.repositories

import com.example.arenamsk.datasources.LocalDataSource
import com.example.arenamsk.datasources.RemoteDataSource
import com.example.arenamsk.models.FeedbackModel
import com.example.arenamsk.models.PlaceFilterModel
import com.example.arenamsk.models.PlaceModel
import com.example.arenamsk.network.models.FeedbackNetworkModel
import com.example.arenamsk.network.models.RequestErrorHandler
import com.example.arenamsk.network.utils.AuthUtils
import com.example.arenamsk.room.tables.Subway
import com.example.arenamsk.utils.toStringTypedArray
import kotlinx.coroutines.CoroutineScope

class PlaceRepository private constructor() : BaseRepository() {

    companion object {
        @Volatile
        private var repositoryInstance: PlaceRepository? = null

        fun getInstance() = repositoryInstance ?: synchronized(this) {
            repositoryInstance ?: PlaceRepository()
        }
    }

    /** Загрузка избранных площадок */
    fun getFavourites(
        success: (response: List<PlaceModel>) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            RemoteDataSource.getFavourites()
        },
        success = success,
        errorHandler = errorHandler
    )

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

    /** Загрузка текущих забронированных площадок */
    //TODO
    fun getCurrentBookedPlaces(
        success: (response: List<PlaceModel>) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            RemoteDataSource.getPlaces()
        },
        success = success,
        errorHandler = errorHandler
    )

    /** Загрузка площадок в истории бронирования */
    //TODO
    fun getBookedHistoryPlaces(
        success: (response: List<PlaceModel>) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            RemoteDataSource.getPlaces()
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

    suspend fun getSubways(success: (response: List<Subway>) -> Unit,
                   errorHandler: RequestErrorHandler) {
        LocalDataSource.getLocalSubwayList().also {
            if (it.isNullOrEmpty()) {
                makeRequest(
                    call = { RemoteDataSource.getSubways() },
                    success = success,
                    errorHandler = errorHandler
                )
            } else {
                success.invoke(it)
            }
        }
    }

    suspend fun getFeedbackList(
        placeId: String,
        success: (feedback: FeedbackNetworkModel) -> Unit,
        errorHandler: RequestErrorHandler
    ) = makeRequest(
        call = {
            with(RemoteDataSource) {
                getFeedbackList(placeId)
            }
        },
        success = success,
        errorHandler = errorHandler
    )

}