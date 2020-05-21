package com.arenabooking.arenamsk.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** Модель с отзывом  */
@Parcelize
data class FeedbackModel(
    //Имя автора
    @SerializedName("authorName")
    val authorName: String = "",

    //Дата в формате yyyy-MM-dd
    @SerializedName("date")
    val date: String = "",

    //Флаг - рекомендована площадка или нет
    @SerializedName("isRecommended")
    val isRecommendation: Boolean = false,

    //Рейтинг поставленный в отзыве
    @SerializedName("rating")
    val rating: Float = 0.0f,

    //Текст отзыва
    @SerializedName("feedback")
    val feedbackText: String = ""

) : Parcelable