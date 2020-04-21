package com.example.arenamsk.network.models.auth

import com.google.gson.annotations.SerializedName

/** DTO с информацией для авторизации пользователя */
data class LogInUserModel(
    @SerializedName("email")
    var email: String = "",

    @SerializedName("number")
    var number: String = "",

    @SerializedName("password")
    var password: String = ""
)