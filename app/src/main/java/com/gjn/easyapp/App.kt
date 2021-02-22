package com.gjn.easyapp

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.gjn.easyapp.easyutils.AutoScreenUtil

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(this)
        AutoScreenUtil.init(this, 411f, true)
    }

    companion object {
        lateinit var instance: Context
    }
}