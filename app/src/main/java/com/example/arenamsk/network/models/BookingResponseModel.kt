package com.example.arenamsk.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** DTO с ссылкой на оплату */
@Parcelize
data class BookingResponseModel(
    //Начало времени бронирования
    @SerializedName("paymentUrl")
    val paymentUrl: String = ""
) : Parcelable