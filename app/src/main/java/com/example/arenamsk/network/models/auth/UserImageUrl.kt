package com.example.arenamsk.network.models.auth

import com.google.gson.annotations.SerializedName

/** DTO с ссылкой на аватарку пользователя */
data class UserImageUrl(
    @SerializedName("imageUrl")
    val imageUrl: String
)