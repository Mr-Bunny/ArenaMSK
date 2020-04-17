package com.example.arenamsk.network.models.auth

import android.os.Parcelable
import com.example.arenamsk.models.FeedbackModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResetPasswordModel(
    @SerializedName("email")
    val email: String? = null,

    @SerializedName("phone")
    val phone: String? = null
) : Parcelable