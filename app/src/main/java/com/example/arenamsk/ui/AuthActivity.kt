package com.example.arenamsk.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.arenamsk.R
import com.example.arenamsk.ui.auth.log_in.LogInFragment

class AuthActivity : AppCompatActivity() {

    companion object {
        const val FRAGMENT_LOG_IN_TAG = "fragment_log_in"
        const val FRAGMENT_SIGN_UP_TAG = "fragment_sign_up"
    }

    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            Toast.makeText(this, getString(R.string.text_close_app_toast_hint), Toast.LENGTH_SHORT).show()

            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }

    private fun openLogInFragment() {
        with(supportFragmentManager.beginTransaction()) {
            replace(
                R.id.auth_fragment_container,
                LogInFragment(),
                FRAGMENT_LOG_IN_TAG
            )
            commit()
        }
    }


}
