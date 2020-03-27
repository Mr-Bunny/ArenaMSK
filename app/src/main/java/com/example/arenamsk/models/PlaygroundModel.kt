package com.example.arenamsk.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaygroundModel(
    @SerializedName("id")
    val id: Long,

    @SerializedName("sport")
    val sport: SportModel? = null,

    @SerializedName("openField")
    val openField: Boolean
) : Parcelable