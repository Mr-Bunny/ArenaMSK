package com.example.arenamsk.network.models

import android.os.Parcelable
import com.example.arenamsk.models.FeedbackModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** DTO с отзывом для отправки на сервер  */
@Parcelize
data class AppFeedbackModel(
    @SerializedName("feedback")
    val feedback: String = ""
) : Parcelable