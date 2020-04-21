package com.example.arenamsk.models

import android.os.Parcelable
import com.example.arenamsk.network.models.ImageModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** Модель площадки (которая СК или СК с одной площадкой) */
@Parcelize
data class PlaceModel(
    //Название площадки
    @SerializedName("name")
    val placeTitle: String = "",

    //Список площадок, если СК
    @SerializedName("playgrounds")
    val playgroundModels: List<PlaygroundModel> = emptyList(),

    //Флаг - площадка в избранном или нет
    @SerializedName("isFavorite")
    var isFavourite: Boolean = false,

    //Флаг - площакдка бронировалась ранее или нет
    @SerializedName("inBookedHistory")
    val inHistory: Boolean = false,

    //Описание
    @SerializedName("description")
    val description: String = "",

    //Время начало работы
    @SerializedName("workDayStartAt")
    val workDayStartAt: String = "",

    //Время окончания работы
    @SerializedName("workDayEndAt")
    val workDayEndAt: String = "",

    //Адрес
    @SerializedName("address")
    val address: String = "",

    //Дистанция от местоположения пользователя до площадки
    @SerializedName("distance")
    var distance: Float = 0.0f,

    //Id площадки
    @SerializedName("id")
    val id: Int = -1,

    //Рейтинг площакди
    @SerializedName("rating")
    val rating: Float = 0.0f,

    //Долгота (координата)
    @SerializedName("longitude")
    val longitude: Double = 0.0,

    //Ширина (координата)
    @SerializedName("latitude")
    val latitude: Double = 0.0,

    //Кол-во отзывов
    @SerializedName("reviewsCount")
    val feedbackNumber: Int = 0,

    //Список url на фото площадки
    @SerializedName("images")
    val images: List<ImageModel> = emptyList(),

    //Список номеров телефонов
    @SerializedName("phoneNumbers")
    val phoneNumbersList: List<String> = emptyList(),

    //Площадь площадки
    @SerializedName("area")
    val totalArea: Int = 0,

    //Кол-во площадок у СК
    @SerializedName("numberOfPlace")
    val numberOfPlaces: Int = 0,

    //Есть ли паркова
    @SerializedName("hasParking")
    val hasParking: Boolean = false,

    //Есть ли инвентарь
    @SerializedName("hasInventory")
    val hasInventory: Boolean = false,

    //Открытое поле или крытое
    @SerializedName("openField")
    val openField: Boolean = false,

    //Есть ли раздевалки
    @SerializedName("hasLockers")
    val hasLockers: Boolean = false,

    //Есть ли душевые
    @SerializedName("hasBaths")
    val hasBaths: Boolean = false

) : Parcelable