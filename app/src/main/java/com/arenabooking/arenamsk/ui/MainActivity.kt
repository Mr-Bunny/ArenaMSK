package com.arenabooking.arenamsk.ui

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.arenabooking.arenamsk.FCMService
import com.arenabooking.arenamsk.R
import com.arenabooking.arenamsk.ui.auth.sign_up.GalleryCallback
import com.arenabooking.arenamsk.utils.ConnectionLiveData
import com.arenabooking.arenamsk.utils.Constants.DOUBLE_CLICK_DELAY
import com.arenabooking.arenamsk.utils.SharedPreferenceManager
import com.arenabooking.arenamsk.utils.SharedPreferenceManager.KEY.FCM_TOKEN_UPDATED
import kotlinx.android.synthetic.main.activity_main.*

/** Главная Activity */
class MainActivity : AppCompatActivity(), LifecycleOwner {

    companion object {
        const val FRAGMENT_EDIT_PROFILE_TAG = "fragment_edit_profile"
    }

    private var doubleBackToExitPressedOnce = false

    private var topLevelDestinations = mutableSetOf(
        R.id.navigation_map,
        R.id.navigation_places,
        R.id.navigation_favourites,
        R.id.navigation_profile
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(navController)

        //При появлении интернета проверяем флаг отправки fcm токена, и если он не был отправлен - отправляем его
        ConnectionLiveData(this).observe(this, Observer { isConnected ->
            isConnected?.let {
                if (isConnected && SharedPreferenceManager.getInstance().getBooleanValue(FCM_TOKEN_UPDATED, false) == false) {
                    with(FCMService) {
                        sendToken(getCurrentFCMToken())
                    }
                }
            }
        })
    }

    /** Переопределяем слушатель кнопки назад. Если открыт фрагмент из нижнего меню - то
     * делаем проверку на двойное нажатие кнопки, после которого закрываем прилоежние,
     * Иначе вызваем super.OnBackPressed*/
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
        } else {
            if (!topLevelDestinations.contains(findNavController(R.id.nav_host_fragment).currentDestination?.id)) {
                super.onBackPressed()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val editProfileFragment = nav_host_fragment.childFragmentManager.fragments[0]

        if (requestCode == AuthActivity.GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                it.data?.let { uri ->
                    val imageStream = contentResolver.openInputStream(uri)

                    editProfileFragment?.let { fragment ->
                        (fragment as GalleryCallback).galleryRequest(
                            BitmapFactory.decodeStream(imageStream)
                        )
                    }
                }
            }
        }
    }
}
