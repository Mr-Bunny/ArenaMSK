package com.arenabooking.arenamsk.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** DTO с информацией о брони */
@Parcelize
data class BookingDateModel(
    //Начало времени бронирования
    @SerializedName("from")
    val from: String = "",

    //Конец времени бронирования
    @SerializedName("to")
    val to: String = "",

    @SerializedName("id")
    val id: Long = -1,

    //Занята площадка или свободна
    @SerializedName("isBooked")
    val isBooked: Boolean = false,

    //true - бронь половины площадки, false - целой
    @SerializedName("isHalfBookingAvailable")
    val isHalfBooking: Boolean = false,

    //Цена
    @SerializedName("price")
    val price: Float = 0f
) : Parcelable