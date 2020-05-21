package com.arenabooking.arenamsk.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** Модель площадки */
@Parcelize
data class PlaygroundModel(
    @SerializedName("id")
    val id: Long,

    //Вид спорта
    @SerializedName("sport")
    val sport: SportModel? = null,

    //Открытая площадка или крытая
    @SerializedName("openField")
    val openField: Boolean
) : Parcelable