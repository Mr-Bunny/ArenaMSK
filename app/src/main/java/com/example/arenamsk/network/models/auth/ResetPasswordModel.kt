package com.example.arenamsk.network.models.auth

import com.google.gson.annotations.SerializedName

data class ResetPasswordModel(
    @SerializedName("mail")
    var mail: String = ""
)