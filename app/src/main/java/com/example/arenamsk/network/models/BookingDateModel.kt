package com.example.arenamsk.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookingDateModel(
    @SerializedName("from")
    val from: String = "",

    @SerializedName("to")
    val to: String = "",

    @SerializedName("id")
    val id: Long = -1,

    @SerializedName("isBooked")
    val isBooked: Boolean = false,

    //true - бронь половины площадки, false - целой
    @SerializedName("isHalfBookingAvailable")
    val isHalfBooking: Boolean = false,

    @SerializedName("price")
    val price: Float = 0f
) : Parcelable