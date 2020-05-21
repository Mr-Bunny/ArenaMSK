package com.arenabooking.arenamsk.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/** Модель с координатами */
@Parcelize
data class CoordinatesModel(val latitude: Double, val longitude: Double): Parcelable