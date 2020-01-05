package com.example.arenamsk.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.arenamsk.ui.auth.log_in.LogInFragment
import com.example.arenamsk.ui.auth.sign_up.SignUpFragmentCallback
import android.graphics.BitmapFactory
import com.example.arenamsk.utils.Constants.DOUBLE_CLICK_DELAY

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
        setContentView(com.example.arenamsk.R.layout.activity_auth)

        openLogInFragment()
    }

    override fun onBackPressed() {
        supportFragmentManager.findFragmentByTag(FRAGMENT_SIGN_UP_TAG)?.let {
            if (it.isVisible) {
                supportFragmentManager.popBackStack()
                return
            }
        }

        if (doubleBackToExitPressedOnce) {
            finishAffinity()
        } else {
            doubleBackToExitPressedOnce = true
            Toast.makeText(
                this,
                getString(com.example.arenamsk.R.string.text_close_app_toast_hint),
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
                        (fragment as SignUpFragmentCallback).galleryRequest(
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
                    signUpFragment?.let { (it as SignUpFragmentCallback).galleryPermissionGranted() }
                } else {
                    signUpFragment?.let { (it as SignUpFragmentCallback).galleryPermissionDenied() }
                }
            }
        }
    }

    fun openApp() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun openLogInFragment() {
        with(supportFragmentManager.beginTransaction()) {
            replace(
                com.example.arenamsk.R.id.auth_fragment_container,
                LogInFragment(),
                FRAGMENT_LOG_IN_TAG
            )
            commit()
        }
    }

}
