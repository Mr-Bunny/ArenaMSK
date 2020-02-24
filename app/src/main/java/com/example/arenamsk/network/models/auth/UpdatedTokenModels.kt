package com.example.arenamsk.network.models.auth

import com.google.gson.annotations.SerializedName

data class UpdatedTokensModel(
    @SerializedName("refreshToken")
    var refreshToken: String = "",

    @SerializedName("accessToken")
    var accessToken: String = "",

    @SerializedName("expiredIn")
    var expiredIn: Int = 0
)