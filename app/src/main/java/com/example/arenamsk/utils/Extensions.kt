package com.example.arenamsk.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.example.arenamsk.App
import com.example.arenamsk.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

fun View.showKeyboard() {
    this.requestFocus()
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.enable() {
    visibility = View.VISIBLE
}

fun View.disable() {
    visibility = View.GONE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun ArrayList<String>.toStringTypedArray(): String? {
    if (this.isNullOrEmpty()) return null

    return this.toString()
        .replace(" ", "")
        .replace("[", "")
        .replace("]", "")
}

fun String?.getSportIcon(): BitmapDescriptor {
    val result = when (this) {
        EnumUtils.Sports.SPORT_FOOTBALL.type, EnumUtils.Sports.SPORT_MINI_FOOTBALL.type -> {
            bitmapDescriptorFromVector(R.drawable.ic_marker_soccer)
        }

        EnumUtils.Sports.SPORT_BASKETBALL.type -> {
            bitmapDescriptorFromVector(R.drawable.ic_marker_basket)
        }

        EnumUtils.Sports.SPORT_VOLLEYBALL.type -> {
            bitmapDescriptorFromVector(R.drawable.ic_marker_volley)
        }

        EnumUtils.Sports.SPORT_TENNIS.type -> {
            bitmapDescriptorFromVector(R.drawable.ic_marker_tennis)
        }

        else -> {
            bitmapDescriptorFromVector(R.drawable.ic_marker_sk)
        }
    }

    return result ?: BitmapDescriptorFactory.defaultMarker()
}

private fun bitmapDescriptorFromVector(vectorResId: Int): BitmapDescriptor? {
    return ContextCompat.getDrawable(App.appContext(), vectorResId)?.run {
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        draw(Canvas(bitmap))
        BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}