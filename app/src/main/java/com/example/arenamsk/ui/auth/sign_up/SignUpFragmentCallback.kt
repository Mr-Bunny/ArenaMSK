package com.example.arenamsk.ui.auth.sign_up

import android.graphics.Bitmap

interface SignUpFragmentCallback {

    fun galleryPermissionGranted()

    fun galleryPermissionDenied()

    fun galleryRequest(bitmap: Bitmap)
}