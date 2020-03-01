package com.example.arenamsk.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** Моделька с информацией о бронировании площадки на определенное время
 * Используется на экране бронирования (ui/booking) */
@Parcelize
data class PlaceBookingModel(
    @SerializedName("time")
    val time: String = "",

    @SerializedName("price")
    val price: Float = 0.0f,

    @SerializedName("status")
    val statusIsFree: Boolean = true

) : Parcelable