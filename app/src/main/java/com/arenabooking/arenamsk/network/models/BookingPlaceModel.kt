package com.arenabooking.arenamsk.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** DTO модель с информацией о брони */
@Parcelize
data class BookingPlaceModel(
    //Дата в формате yyyy-MM-dd
    @SerializedName("date")
    val date: String = "",

    @SerializedName("name")
    var name: String = "",

    @SerializedName("email")
    var email: String = "",

    @SerializedName("phone")
    var phone: String = "",

    //Список id времен, которые мы хотим забронировать
    @SerializedName("bookingsId")
    val bookingsId: List<String> = emptyList()
) : Parcelable