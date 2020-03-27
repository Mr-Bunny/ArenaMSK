package com.example.arenamsk.models

import android.os.Parcelable
import com.example.arenamsk.network.models.ImageModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceModel(
    @SerializedName("name")
    val placeTitle: String = "",

    @SerializedName("playgrounds")
    val playgroundModels: List<PlaygroundModel> = emptyList(),

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
    var distance: Float = 0.0f,

    @SerializedName("id")
    val id: Int = -1,

    @SerializedName("rating")
    val rating: Float = 0.0f,

    @SerializedName("longitude")
    val longitude: Double = 0.0,

    @SerializedName("latitude")
    val latitude: Double = 0.0,

    @SerializedName("reviewsCount")
    val feedbackNumber: Int = 0,

    @SerializedName("images")
    val images: List<ImageModel> = emptyList(),

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