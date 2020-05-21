package com.arenabooking.arenamsk.ui.auth.sign_up

import android.graphics.Bitmap

/** Колбэк при работе с галереей */
interface GalleryCallback {

    fun galleryPermissionGranted()

    fun galleryPermissionDenied()

    fun galleryRequest(bitmap: Bitmap)
}