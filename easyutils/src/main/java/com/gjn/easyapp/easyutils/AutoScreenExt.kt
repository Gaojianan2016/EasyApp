package com.gjn.easyapp.easyutils

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.res.Configuration

/**
 * 今日头条适配方式
 * */
object AutoScreenUtil {
    private const val LINE = "---------------------------------"

    private lateinit var application: Application
    private var width = -1f
    private var isDebug = false

    private var defaultDensity = -1f
    private var defaultDensityDpi = -1
    private var defaultScaledDensity = -1f

    private var changeDensity = -1f
    private var changeDensityDpi = -1
    private var changeScaledDensity = -1f

    fun init(application: Application, width: Float, isDebug: Boolean = false) {
        this.application = application
        this.width = width
        this.isDebug = isDebug
        log(LINE)
        log("当前强制width为$width")
        initApplication()
        log(LINE)
    }

    private fun initApplication() {
        val metrics = application.resources.displayMetrics
        if (defaultDensity == -1f) {
            defaultDensity = metrics.density
            changeDensity = metrics.widthPixels / width

            defaultDensityDpi = metrics.densityDpi
            changeDensityDpi = (changeDensity * 160).toInt()

            defaultScaledDensity = metrics.scaledDensity
            changeScaledDensity = changeDensity * (defaultScaledDensity / defaultDensity)
        }

        application.registerComponentCallbacks(object : ComponentCallbacks2 {
            override fun onConfigurationChanged(newConfig: Configuration) {
                if (newConfig.fontScale > 0) {
                    log("fontScale is change ----> ${newConfig.fontScale}")
                    defaultScaledDensity = defaultDensity * newConfig.fontScale
                    changeScaledDensity = changeDensity * newConfig.fontScale
                }
            }

            override fun onLowMemory() {
                log("onLowMemory")
            }

            override fun onTrimMemory(level: Int) {
                log("onTrimMemory $level")
            }
        })
        log("defaultDensity = $defaultDensity, defaultDensityDpi = $defaultDensityDpi, defaultScaledDensity = $defaultScaledDensity ")
        log("changeDensity = $changeDensity, changeDensityDpi = $changeDensityDpi, changeScaledDensity = $changeScaledDensity ")
    }

    fun setCustomDensity(activity: Activity) {
        val metrics = activity.resources.displayMetrics
        val density: Float
        val densityDpi: Int
        val scaledDensity: Float
        when (activity) {
            is IAutoCancel -> {
                //取消自动设置
                density = defaultDensity
                densityDpi = defaultDensityDpi
                scaledDensity = defaultScaledDensity
                log("${activity::class.java.simpleName} default density")
            }
            is IAutoChange -> {
                //改变自动设置
                density = metrics.widthPixels / activity.newWidth()
                densityDpi = (density * 160).toInt()
                scaledDensity = density * (defaultScaledDensity / defaultDensity)
                log("${activity::class.java.simpleName} change new density ${activity.newWidth()}")
            }
            else -> {
                density = changeDensity
                densityDpi = changeDensityDpi
                scaledDensity = changeScaledDensity
                log("${activity::class.java.simpleName} change density $width")
            }
        }
        metrics.density = density
        metrics.densityDpi = densityDpi
        metrics.scaledDensity = scaledDensity
    }

    private fun log(content: String?) {
        if (isDebug) logD(content, "AutoScreenUtil")
    }
}

interface IAutoCancel

interface IAutoChange {
    fun newWidth(): Float
}