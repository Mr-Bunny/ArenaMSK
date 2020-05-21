package com.arenabooking.arenamsk.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** DTO с firebase токеном */
@Parcelize
data class FCMTokenModel(
    @SerializedName("token")
    val token: String = ""
): Parcelable