package com.example.arenamsk.network.models.auth

import com.google.gson.annotations.SerializedName

data class UserImageUrl(
    @SerializedName("imageUrl")
    val imageUrl: String
)