package com.example.arenamsk.network.models.auth

import com.google.gson.annotations.SerializedName

/** DTO с новыми токенами */
data class UpdatedTokensModel(
    @SerializedName("refreshToken")
    var refreshToken: String = "",

    @SerializedName("accessToken")
    var accessToken: String = "",

    @SerializedName("expiredIn")
    var expiredIn: Int = 0
)