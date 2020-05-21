package com.arenabooking.arenamsk.network.models.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** DTO с инфомрацией для смены пароля */
@Parcelize
data class PasswordChangeDto(
    @SerializedName("currentPassword")
    val currentPassword: String,
    @SerializedName("newPassword")
    val newPassword: String
) : Parcelable