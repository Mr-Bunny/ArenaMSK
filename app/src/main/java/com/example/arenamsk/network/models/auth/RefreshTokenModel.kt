package com.example.arenamsk.network.models.auth

import com.google.gson.annotations.SerializedName

data class RefreshTokenModel(
    @SerializedName("refreshToken")
    var refreshToken: String = ""
)