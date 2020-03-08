package com.example.arenamsk.ui

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.arenamsk.R
import com.example.arenamsk.utils.Constants.DOUBLE_CLICK_DELAY

class MainActivity : AppCompatActivity(), LifecycleOwner {

    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
        } else {
            doubleBackToExitPressedOnce = true
            Toast.makeText(
                this,
                getString(R.string.text_close_app_toast_hint),
                Toast.LENGTH_SHORT
            ).show()

            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, DOUBLE_CLICK_DELAY)
        }
    }
}
