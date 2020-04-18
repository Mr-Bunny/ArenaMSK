package com.example.arenamsk.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookingPlaceModel(
    @SerializedName("date")
    val date: String = "",

    @SerializedName("bookingsId")
    val bookingsId: List<String> = emptyList()
) : Parcelable