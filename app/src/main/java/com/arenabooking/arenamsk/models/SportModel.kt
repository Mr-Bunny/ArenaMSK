package com.arenabooking.arenamsk.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** Модель вида спорта с названием вида спорта */
@Parcelize
data class SportModel(
    @SerializedName("name")
    val name: String
) : Parcelable