package com.arenabooking.arenamsk.models

import android.os.Parcelable
import com.arenabooking.arenamsk.room.tables.Subway
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** Модель фильтра */
@Parcelize
data class PlaceFilterModel(
    //Есть ли душевые
    @SerializedName("hasBaths")
    var hasBaths: Boolean = false,

    //Есть ли инвентарь
    @SerializedName("hasInventory")
    var hasInventory: Boolean = false,

    //Есть ли раздевалка
    @SerializedName("hasLockers")
    var hasLockers: Boolean = false,

    //Есть ли парковка
    @SerializedName("hasParking")
    var hasParking: Boolean = false,

    //Открытое поле или крытое
    @SerializedName("openField")
    var openField: Boolean = false,

    //Минимальная цена за бронь
    @SerializedName("priceFrom")
    var priceFrom: Int = 0,

    //Максимальная цена за бронь
    @SerializedName("priceTo")
    var priceTo: Int = 5000,

    //Список видов спорта (было решено ограничить выбор только одним видом спорта)
    @SerializedName("sports")
    var sportList: ArrayList<String>? = ArrayList(),

    //Станция метро
    @SerializedName("subways")
    var subways: Subway? = null

) : Parcelable