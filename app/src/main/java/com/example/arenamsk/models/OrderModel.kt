package com.example.arenamsk.models

import android.os.Parcelable
import com.example.arenamsk.network.models.BookingsModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderModel(
    @SerializedName("amount")
    val amount: String = "",

    @SerializedName("date")
    val date: String = "",

    @SerializedName("from")
    val from: String = "",

    @SerializedName("to")
    val to: String = "",

    @SerializedName("booking")
    val booking: List<BookingsModel> = emptyList(),

    @SerializedName("playground")
    val playground: PlaygroundModel? = null,

    @SerializedName("place")
    val place: PlaceModel? = null

) : Parcelable