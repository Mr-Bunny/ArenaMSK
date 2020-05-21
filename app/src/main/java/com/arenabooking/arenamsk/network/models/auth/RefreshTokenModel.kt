package com.arenabooking.arenamsk.network.models.auth

import com.google.gson.annotations.SerializedName

/** DTO для обновления токенов */
data class RefreshTokenModel(
    @SerializedName("refreshToken")
    var refreshToken: String = ""
)