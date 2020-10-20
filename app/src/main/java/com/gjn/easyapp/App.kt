package com.gjn.easyapp

import android.app.Application
import android.content.Context

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object{
        lateinit var INSTANCE: Context
    }
}