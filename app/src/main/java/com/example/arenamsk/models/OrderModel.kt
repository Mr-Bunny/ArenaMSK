package com.example.arenamsk.models

import android.os.Parcelable
import com.example.arenamsk.network.models.BookingsModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** Модель с информацией о текущем заказе */
@Parcelize
data class OrderModel(
    //Сумма заказа
    @SerializedName("amount")
    val amount: String = "",

    //Дата заказа
    @SerializedName("date")
    val date: String = "",

    //Время начало брони (используется из booking)
    @SerializedName("from")
    val from: String = "",

    //Время окончания брони (используется из booking)
    @SerializedName("to")
    val to: String = "",

    //Список забронированного времени
    @SerializedName("booking")
    val booking: List<BookingsModel> = emptyList(),

    //Забронированная площадка
    @SerializedName("playground")
    val playground: PlaygroundModel? = null,

    //Забронированное место
    @SerializedName("place")
    val place: PlaceModel? = null

) : Parcelable