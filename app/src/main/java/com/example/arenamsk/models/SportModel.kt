package com.example.arenamsk.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SportModel(
    @SerializedName("name")
    val name: String
) : Parcelable