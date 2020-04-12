package com.example.arenamsk.models

import android.os.Parcelable
import com.example.arenamsk.network.models.BookingDateModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** Моделька с информацией о бронировании площадки на определенное время
 * Используется на экране бронирования (ui/booking) */
@Parcelize
data class PlaceBookingModel(
    @SerializedName("date")
    val date: Long = 0,

    @SerializedName("bookings")
    val bookings: List<BookingDateModel> = emptyList()

) : Parcelable