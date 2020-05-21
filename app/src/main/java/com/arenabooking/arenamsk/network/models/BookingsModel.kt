package com.arenabooking.arenamsk.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** DTO, которое хранится в OrderModel
 * Обозначает время площадки, которое мы забронирровали */
@Parcelize
data class BookingsModel(
    //Время начала брони
    @SerializedName("bookingFrom")
    val from: String = "",

    //Время окончания брони
    @SerializedName("bookingTo")
    val to: String = "",

    //true - бронь половины площадки, false - целой
    @SerializedName("half")
    val isHalfBooking: Boolean = false,

    //Цена за бронь
    @SerializedName("price")
    val price: Float = 0f
) : Parcelable