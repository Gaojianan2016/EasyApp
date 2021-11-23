package com.gjn.easyapp.easyutils

import android.view.View

/**
 * 单次点击
 * */
fun View.click(block: View.() -> Unit) {
    setOnClickListener { it.block() }
}

/**
 * 长按点击
 * */
fun View.clickLong(block: View.() -> Boolean) {
    setOnLongClickListener {
        return@setOnLongClickListener it.block()
    }
}

/**
 * 防抖点击
 * */
fun View.debouncingClick(duration: Long = 1000L, block: View.() -> Unit) {
    setOnClickListener(object : OnDebouncingClickListener(duration) {
        override fun onDebouncingClick(v: View?) {
            v?.block()
        }
    })
}

/**
 * 批量设置单次点击
 * */
fun Array<out View?>.setOnClickListeners(listener: View.OnClickListener) {
    forEach { it?.setOnClickListener(listener) }
}

/**
 * 批量设置长按点击
 * */
fun Array<out View?>.setOnLongClickListeners(listener: View.OnLongClickListener) {
    forEach { it?.setOnLongClickListener(listener) }
}

/**
 * 批量设置防抖点击
 * */
fun Array<out View?>.setOnDebouncingClickListeners(listener: OnDebouncingClickListener) {
    forEach { it?.setOnClickListener(listener) }
}

/**
 * 防抖点击事件
 * */
abstract class OnDebouncingClickListener(private val duration: Long = 1000L) :
    View.OnClickListener {

    private var enabled = true

    private val enableAgainRunnable = Runnable { enabled = true }

    override fun onClick(v: View?) {
        if (enabled) {
            enabled = false
            v?.postDelayed(enableAgainRunnable, duration)
            onDebouncingClick(v)
        }
    }

    abstract fun onDebouncingClick(v: View?)
}