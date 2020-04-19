package com.example.arenamsk.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** DTO, которое хранится в OrderModel
 * Обозначает время площадки, которое мы забронирровали */
@Parcelize
data class BookingsModel(
    @SerializedName("bookingFrom")
    val from: String = "",

    @SerializedName("bookingTo")
    val to: String = "",

    //true - бронь половины площадки, false - целой
    @SerializedName("half")
    val isHalfBooking: Boolean = false,

    @SerializedName("price")
    val price: Float = 0f
) : Parcelable