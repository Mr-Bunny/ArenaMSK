package com.example.arenamsk.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceModel(
    @SerializedName("title")
    var title: String = "",

    @SerializedName("is_favourite")
    var isFavourite: Boolean = false,

    @SerializedName("description")
    var description: String = "",

    @SerializedName("work_time")
    var workTime: String = "",

    @SerializedName("address")
    var address: String = "",

    @SerializedName("distance")
    var distance: Float = 0.0f,

    @SerializedName("id")
    var id: Int = -1,

    @SerializedName("rating")
    var rating: Float = 0.0f,

    @SerializedName("feedback_number")
    var feedbackNumber: Int = 0,

    @SerializedName("imageUrl")
    var imagesUrl: List<String> = emptyList(),

    @SerializedName("phone_numbers")
    var phoneNumbersList: List<String> = emptyList(),

    @SerializedName("total_area")
    var totalArea: Int = 0,

    @SerializedName("number_of_places")
    var numberOfPlaces: Int = 0,

    @SerializedName("has_parking")
    var hasParking: Boolean = false,

    @SerializedName("has_inventory")
    var hasInventory: Boolean = false,

    @SerializedName("open_field")
    var openField: Boolean = false,

    @SerializedName("has_lockers")
    var hasLockers: Boolean = false,

    @SerializedName("has_baths")
    var hasBaths: Boolean = false

) : Parcelable