package com.example.arenamsk.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

//TODO тут должны быть настройки: вид спорта, метро, открытая площадка или нет и т.п.
// на основе этого и сортируем список
@Parcelize
data class PlaceFilterModel(
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

    @SerializedName("sports")
    var sportList: ArrayList<String>? = ArrayList(),

    @SerializedName("imageUrl")
    var imagesUrl: List<String> = emptyList()

) : Parcelable