package com.example.arenamsk.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** DTO с ссылками на автарку пользователя */
@Parcelize
data class ImageModel(
    @SerializedName("id")
    val id: Int = -1,

    @SerializedName("fullImage")
    val fullImage: String = "",

    @SerializedName("thumbImage")
    val thumbImage: String = "",

    @SerializedName("uploadTimestamp")
    val uploadTimestamp: Int? = 0
): Parcelable