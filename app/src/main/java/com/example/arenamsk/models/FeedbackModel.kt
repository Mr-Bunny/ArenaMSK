package com.example.arenamsk.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeedbackModel(
    @SerializedName("authorName")
    val authorName: String = "",

    @SerializedName("reviewDate")
    val date: String = "",

    @SerializedName("isRecommended")
    val isRecommendation: Boolean = false,

    @SerializedName("rating")
    val rating: Float = 0.0f,

    @SerializedName("feedback")
    val feedbackText: String = ""

) : Parcelable