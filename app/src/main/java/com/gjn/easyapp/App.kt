package com.gjn.easyapp

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.gjn.easyapp.easyutils.HookActivityUtils

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        //钩子
        HookActivityUtils().hookStartActivity(instance, ProxyActivity::class.java)
        MultiDex.install(this)
    }

    companion object {
        lateinit var instance: Context
    }
}