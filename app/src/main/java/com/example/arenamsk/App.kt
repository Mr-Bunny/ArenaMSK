package com.example.arenamsk

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication

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