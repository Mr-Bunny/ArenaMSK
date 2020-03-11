package com.example.arenamsk.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceModel(
    @SerializedName("name")
    val title: String = "",

    @SerializedName("isFavorite")
    var isFavourite: Boolean = false,

    @SerializedName("inBookedHistory")
    val inHistory: Boolean = false,

    @SerializedName("description")
    val description: String = "",

    @SerializedName("workDayStartAt")
    val workDayStartAt: String = "",

    @SerializedName("workDayEndAt")
    val workDayEndAt: String = "",

    @SerializedName("address")
    val address: String = "",

    @SerializedName("distance")
    val distance: Float = 0.0f,

    @SerializedName("id")
    val id: Int = -1,

    @SerializedName("rating")
    val rating: Float = 0.0f,

    @SerializedName("reviewsCount")
    val feedbackNumber: Int = 0,

    @SerializedName("images")
    val imagesUrl: List<String> = emptyList(),

    @SerializedName("phoneNumbers")
    val phoneNumbersList: List<String> = emptyList(),

    @SerializedName("area")
    val totalArea: Int = 0,

    @SerializedName("numberOfPlace")
    val numberOfPlaces: Int = 0,

    @SerializedName("hasParking")
    val hasParking: Boolean = false,

    @SerializedName("hasInventory")
    val hasInventory: Boolean = false,

    @SerializedName("openField")
    val openField: Boolean = false,

    @SerializedName("hasLockers")
    val hasLockers: Boolean = false,

    @SerializedName("hasBaths")
    val hasBaths: Boolean = false,

    @SerializedName("reviews")
    val feedbackList: List<FeedbackModel> = emptyList()

) : Parcelable