package com.arenabooking.arenamsk.network.models.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** DTO с информацией для подтверждения смены пароля */
@Parcelize
data class ResetPasswordModel(
    //email пользователя, указанный при регистрации, null - если был использован номер телефона
    @SerializedName("email")
    val email: String? = null,

    //номер телефона пользователя, указанный при регистрации, null - если был использован email
    @SerializedName("phone")
    val phone: String? = null
) : Parcelable