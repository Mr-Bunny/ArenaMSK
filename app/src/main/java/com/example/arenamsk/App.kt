package com.example.arenamsk

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

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