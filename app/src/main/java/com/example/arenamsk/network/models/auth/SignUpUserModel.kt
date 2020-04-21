package com.example.arenamsk.network.models.auth

import com.google.gson.annotations.SerializedName

/** DTO для регистрации пользователя */
data class SignUpUserModel(
    @SerializedName("firstName")
    var firstName: String = "",

    @SerializedName("email")
    var email: String? = "",

    @SerializedName("number")
    var number: String = "",

    @SerializedName("password")
    var password: String = ""
)