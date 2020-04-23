package com.example.arenamsk.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

/** Класс помощник для работы с разрешениями */
object PermissionUtils {

    fun checkForPermission(
        activity: Activity,
        permission: String,
        granted: () -> Unit,
        dismiss: () -> Unit
    ) {
        if (isNotGranted(activity, permission)) {
            dismiss.invoke()
        } else {
            granted.invoke()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun requestForPermission(activity: Activity, permission: String, requestCode: Int) {
        if (isNotGranted(activity, permission)) {
            activity.requestPermissions(arrayOf(permission), requestCode)
        }
    }

    private fun isNotGranted(activity: Activity, permission: String) =
        (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
            activity,
            permission
        ) != PackageManager.PERMISSION_GRANTED)
}