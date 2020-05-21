package com.arenabooking.arenamsk.ui.place_detail_feedback

import androidx.lifecycle.MutableLiveData
import com.arenabooking.arenamsk.models.FeedbackModel
import com.arenabooking.arenamsk.network.models.ApiError
import com.arenabooking.arenamsk.network.models.RequestErrorHandler
import com.arenabooking.arenamsk.repositories.PlaceRepository
import com.arenabooking.arenamsk.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaceDetailFeedbackViewModel: BaseViewModel() {

    private var feedbackListLiveData = MutableLiveData<MutableList<FeedbackModel>>()

    private val repository = PlaceRepository.getInstance()

    fun getFeedbackListLiveData() = feedbackListLiveData

    fun getFeedbackList(placeId: String) {
        launch(Dispatchers.IO) {
            repository.getFeedbackList(placeId,
                success = {
                    feedbackListLiveData.value = it.feedbackList as MutableList<FeedbackModel>
                },
                errorHandler = object: RequestErrorHandler {
                    override suspend fun networkUnavailableError() {
                        setDataError()
                    }

                    override suspend fun requestFailedError(error: ApiError?) {
                        setDataError()
                    }

                    override suspend fun requestSuccessButResponseIsNull() {
                        setDataError()
                    }

                    override suspend fun timeoutException() {
                        setDataError()
                    }
                })
        }
    }

    private suspend fun setDataError() {
        withContext(Dispatchers.Main) {
            feedbackListLiveData.value = null
        }
    }

}