package com.arenabooking.arenamsk.network.models

import android.os.Parcelable
import com.arenabooking.arenamsk.models.FeedbackModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** DTO со списком отзывов о площадке */
@Parcelize
data class FeedbackNetworkModel(
    @SerializedName("content")
    val feedbackList: List<FeedbackModel> = emptyList()
) : Parcelable