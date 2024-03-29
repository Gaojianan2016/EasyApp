package com.gjn.easyapp

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.gjn.easyapp.easybase.BaseLog
import com.gjn.easyapp.easytoaster.ToastUtil
import com.gjn.easyapp.easyutils.AutoScreenUtil
import com.gjn.easyapp.easyutils.MMKVUtil
import com.tencent.mmkv.MMKV

class App : Application() {

    var eHandler: Thread.UncaughtExceptionHandler? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(this)
        AutoScreenUtil.init(this, 411f, true)
        ToastUtil.init(applicationContext)
        BaseLog.isDebug = BuildConfig.DEBUG

        MMKV.initialize(this)
        mmkvUtil = MMKVUtil.getInstance(MMKV.defaultMMKV())

        eHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->

            BaseLog.e("错误线程 ${t.name}", tr = e)

            eHandler?.uncaughtException(t, e)
        }
    }

    companion object {
        lateinit var instance: Application
        lateinit var mmkvUtil: MMKVUtil
    }
}