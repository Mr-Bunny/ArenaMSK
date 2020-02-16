package com.example.arenamsk.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceModel(
    @SerializedName("title")
    val title: String = "",

    @SerializedName("is_favourite")
    val isFavourite: Boolean = false,

    @SerializedName("in_history")
    val inHistory: Boolean = false,

    @SerializedName("description")
    val description: String = "",

    @SerializedName("work_time")
    val workTime: String = "",

    @SerializedName("address")
    val address: String = "",

    @SerializedName("distance")
    val distance: Float = 0.0f,

    @SerializedName("id")
    val id: Int = -1,

    @SerializedName("rating")
    val rating: Float = 0.0f,

    @SerializedName("feedback_number")
    val feedbackNumber: Int = 0,

    @SerializedName("imageUrl")
    val imagesUrl: List<String> = emptyList(),

    @SerializedName("phone_numbers")
    val phoneNumbersList: List<String> = emptyList(),

    @SerializedName("total_area")
    val totalArea: Int = 0,

    @SerializedName("number_of_places")
    val numberOfPlaces: Int = 0,

    @SerializedName("has_parking")
    val hasParking: Boolean = false,

    @SerializedName("has_inventory")
    val hasInventory: Boolean = false,

    @SerializedName("open_field")
    val openField: Boolean = false,

    @SerializedName("has_lockers")
    val hasLockers: Boolean = false,

    @SerializedName("has_baths")
    val hasBaths: Boolean = false,

    @SerializedName("feedback_list")
    val feedbackList: List<FeedbackModel> = emptyList()

) : Parcelable