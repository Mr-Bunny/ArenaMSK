package com.example.arenamsk

import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.example.arenamsk.network.utils.AuthUtils
import com.example.arenamsk.ui.AuthActivity

class App : MultiDexApplication() {

    companion object {
        private lateinit var instance: App

        fun appContext() = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}