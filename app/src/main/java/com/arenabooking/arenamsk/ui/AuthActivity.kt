package com.arenabooking.arenamsk.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.arenabooking.arenamsk.ui.auth.log_in.LogInFragment
import com.arenabooking.arenamsk.ui.auth.sign_up.GalleryCallback
import android.graphics.BitmapFactory
import com.arenabooking.arenamsk.network.utils.AuthUtils
import com.arenabooking.arenamsk.utils.Constants.DOUBLE_CLICK_DELAY

/** Activity авторизации и регистрации */
class AuthActivity : AppCompatActivity() {

    companion object {
        const val FRAGMENT_LOG_IN_TAG = "fragment_log_in"
        const val FRAGMENT_SIGN_UP_TAG = "fragment_sign_up"
        const val GALLERY_PERMISSION_REQUEST_CODE: Int = 1010
        const val GALLERY_REQUEST_CODE: Int = 1011
    }

    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.arenabooking.arenamsk.R.layout.activity_auth)

        //Если пользователь авторизирован - открываем приложение
        if (AuthUtils.isUserAuthorized()) {
            openApp()
        } else {
            openLogInFragment()
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.findFragmentByTag(FRAGMENT_SIGN_UP_TAG)?.let {
            if (it.isVisible) {
                supportFragmentManager.popBackStack()
                return
            }
        }

        //Выход по двойному нажатию кнопки назад
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
        } else {
            doubleBackToExitPressedOnce = true
            Toast.makeText(
                this,
                getString(com.arenabooking.arenamsk.R.string.text_close_app_toast_hint),
                Toast.LENGTH_SHORT
            ).show()

            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, DOUBLE_CLICK_DELAY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val signUpFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_SIGN_UP_TAG)?.let {
            if (it.isVisible) {
                it
            } else null
        }

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                it.data?.let { uri ->
                    val imageStream = contentResolver.openInputStream(uri)

                    signUpFragment?.let { fragment ->
                        (fragment as GalleryCallback).galleryRequest(
                            BitmapFactory.decodeStream(imageStream)
                        )
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            GALLERY_PERMISSION_REQUEST_CODE -> {
                val signUpFragment =
                    supportFragmentManager.findFragmentByTag(FRAGMENT_SIGN_UP_TAG)?.let {
                        if (it.isVisible) {
                            it
                        } else null
                    }

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    signUpFragment?.let { (it as GalleryCallback).galleryPermissionGranted() }
                } else {
                    signUpFragment?.let { (it as GalleryCallback).galleryPermissionDenied() }
                }
            }
        }
    }

    fun openApp() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    /** Открываем фрагмент авторизации */
    private fun openLogInFragment() {
        with(supportFragmentManager.beginTransaction()) {
            replace(
                com.arenabooking.arenamsk.R.id.auth_fragment_container,
                LogInFragment(),
                FRAGMENT_LOG_IN_TAG
            )
            commit()
        }
    }

}
