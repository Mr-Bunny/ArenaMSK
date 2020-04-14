package com.example.arenamsk.models

import android.os.Parcelable
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

    @SerializedName("playground")
    val playground: PlaygroundModel? = null

) : Parcelable