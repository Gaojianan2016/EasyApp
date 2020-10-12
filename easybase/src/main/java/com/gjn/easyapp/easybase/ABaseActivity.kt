package com.gjn.easyapp.easybase

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.gjn.easyapp.easytoaster.ToastUtil
import com.gjn.easyapp.easyutils.ActivityUtils
import com.gjn.easyapp.easyutils.AppManager

abstract class ABaseActivity
    : AppCompatActivity(), UIEvent {

    protected lateinit var mActivity: AppCompatActivity
    protected lateinit var mContext: Context
    protected lateinit var mBundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        AppManager.instance.addActivity(this)
        preCreate()
        super.onCreate(savedInstanceState)
        if (layoutId() != View.NO_ID) {
            setContentView(layoutId())
        }
        mActivity = this
        mContext = this
        mBundle = intent.extras ?: Bundle()
        onBundle()
        init()
        initView()
        initData()
    }

    protected open fun preCreate() {
    }

    protected open fun onBundle() {
    }

    protected open fun init() {
    }

    abstract fun layoutId(): Int

    abstract fun initView()

    abstract fun initData()

    override fun showToast(msg: String?) {
        ToastUtil.instanceApplication(mContext).showToast(msg)
    }

    override fun showNextActivity(cls: Class<*>, bundle: Bundle?) {
        ActivityUtils.showNextActivity(mActivity, cls, bundle)
    }

    override fun toNextActivity(cls: Class<*>, bundle: Bundle?) {
        ActivityUtils.toNextActivity(mActivity, cls, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.instance.removeActivity(this)
    }
}