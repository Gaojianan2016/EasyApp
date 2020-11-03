package com.gjn.easyapp

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        MultiDex.install(this)
    }

    companion object{
        lateinit var INSTANCE: Context
    }
}