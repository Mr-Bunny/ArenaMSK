package com.example.arenamsk.room.tables

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** Сущность пользователя в БД */
@Parcelize
@Entity(tableName = "userTable")
data class User(
    @SerializedName("userId")
    @PrimaryKey
    var userId: Int = -1,

    @SerializedName("firstName")
    var firstName: String? = "",

    @SerializedName("number")
    var number: String? = "",

    @SerializedName("imageUrl")
    var imageUrl: String? = "",

    @SerializedName("email")
    var email: String? = ""
): Parcelable
