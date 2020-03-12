package com.example.arenamsk.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceFilterModel(
    @SerializedName("hasBaths")
    var hasBaths: Boolean = false,

    @SerializedName("hasInventory")
    var hasInventory: Boolean = false,

    @SerializedName("hasLockers")
    var hasLockers: Boolean = false,

    @SerializedName("hasParking")
    var hasParking: Boolean = false,

    @SerializedName("openField")
    var openField: Boolean = false,

    @SerializedName("priceFrom")
    var priceFrom: Int = 0,

    @SerializedName("priceTo")
    var priceTo: Int = 100000,

    @SerializedName("sports")
    var sportList: ArrayList<String>? = ArrayList(),

    @SerializedName("subways")
    var subways: ArrayList<String>? = ArrayList()

) : Parcelable