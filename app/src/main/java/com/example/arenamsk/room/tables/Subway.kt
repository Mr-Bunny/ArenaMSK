package com.example.arenamsk.room.tables

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "subwaysTable")
data class Subway(
    @SerializedName("id")
    @PrimaryKey
    var id: Int = 0,

    @SerializedName("name")
    var name: String? = "",

    @SerializedName("longitude")
    var longitude: Float? = 0f,

    @SerializedName("latitude")
    var latitude: Float? = 0f
): Parcelable
