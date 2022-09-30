package com.gjn.easyapp.easyutils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import java.util.*

interface IActivityResultListener {
    fun onActivityResult(resultCode: Int, data: Intent?)

    fun onException(tr: Throwable)
}

internal class ActivityResultFragment : Fragment() {

    private val mFragmentListeners: SparseArray<IActivityResultListener> = SparseArray()

    private val mCodeGenerator = Random()

    init {
        retainInstance = true
    }

    fun startActivityForResult(
        intent: Intent,
        extras: Bundle?,
        listener: IActivityResultListener?
    ) {
        val requestCode = randomRequestCode()
        mFragmentListeners.put(requestCode, listener)
        startActivityForResult(intent.apply { extras?.let { putExtras(it) } }, requestCode)
    }

    /**
     * 随机生成code
     */
    private fun randomRequestCode(): Int {
        var requestCode: Int
        var tryCount = 0
        do {
            requestCode = mCodeGenerator.nextInt(0x0000FFFF)
            tryCount++
        } while (mFragmentListeners.indexOfKey(requestCode) >= 0 && tryCount < MAX_TYR)
        return requestCode
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val listener = mFragmentListeners[requestCode]
        mFragmentListeners.remove(requestCode)
        try {
            listener?.onActivityResult(resultCode, data)
        } catch (tr: Throwable) {
            listener?.onException(tr)
        }
    }

    companion object {
        private const val MAX_TYR = 10
    }
}

class ActivityResultHelper {
    private var activity: Activity? = null
    private var fm: FragmentManager? = null

    private constructor(activity: FragmentActivity) {
        this.activity = activity
        fm = activity.supportFragmentManager
    }

    private constructor(fragment: Fragment) {
        this.activity = fragment.activity
        fm = fragment.childFragmentManager
    }

    fun startActivityForResult(
        intent: Intent,
        extras: Bundle?,
        listener: IActivityResultListener?
    ) {
        getActivityResultFragment().startActivityForResult(intent, extras, listener)
    }

    private fun getActivityResultFragment(): ActivityResultFragment {
        var fragment = fm!!.findFragmentByTag(TAG_ACTIVITY_RESULT)
        if (fragment == null) {
            fragment = ActivityResultFragment()
            fm!!.run {
                beginTransaction().add(fragment, TAG_ACTIVITY_RESULT).commitAllowingStateLoss()
                executePendingTransactions()
            }
        }
        return fragment as ActivityResultFragment
    }

    abstract class SimpleActivityResultListener : IActivityResultListener {
        override fun onActivityResult(resultCode: Int, data: Intent?) { /**/ }

        override fun onException(tr: Throwable) { /**/ }
    }

    companion object {
        const val TAG_ACTIVITY_RESULT = "TAG_ACTIVITY_RESULT"

        fun newInstance(activity: FragmentActivity) = ActivityResultHelper(activity)

        fun newInstance(fragment: Fragment) = ActivityResultHelper(fragment)
    }
}

/**
 * 快速Activity结果获取
 * */
fun FragmentActivity.quickActivityResult(
    intent: Intent,
    extras: Bundle? = null,
    block: (Int, Intent?) -> Unit
) {
    try {
        ActivityResultHelper.newInstance(this).startActivityForResult(intent, extras, object : ActivityResultHelper.SimpleActivityResultListener() {
            override fun onActivityResult(resultCode: Int, data: Intent?) {
                block.invoke(resultCode, data)
            }
        })
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 快速Activity结果获取
 * */
fun Fragment.quickActivityResult(
    intent: Intent,
    extras: Bundle? = null,
    block: (Int, Intent?) -> Unit
) {
    try {
        ActivityResultHelper.newInstance(this).startActivityForResult(intent, extras, object : ActivityResultHelper.SimpleActivityResultListener() {
            override fun onActivityResult(resultCode: Int, data: Intent?) {
                block.invoke(resultCode, data)
            }
        })
    } catch (e: Exception) {
        e.printStackTrace()
    }
}