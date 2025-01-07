package com.example.mastermindAndroid

import android.app.Application
import com.example.mastermindAndroid.data.AppContainer
import com.example.mastermindAndroid.data.AppDataContainer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GameApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}