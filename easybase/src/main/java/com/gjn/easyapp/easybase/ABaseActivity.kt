package com.gjn.easyapp.easybase

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gjn.easyapp.easydialoger.EasyDialogManager
import com.gjn.easyapp.easydialoger.base.BaseDialogFragment
import com.gjn.easyapp.easytoaster.ToastUtil
import com.gjn.easyapp.easyutils.ActivityUtils
import com.gjn.easyapp.easyutils.AppManager

abstract class ABaseActivity : AppCompatActivity(), UIEvent {

    protected lateinit var mActivity: AppCompatActivity
    protected lateinit var mContext: Context
    protected lateinit var mBundle: Bundle
    protected lateinit var mDialogManager: EasyDialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        AppManager.instance.addActivity(this)
        preCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        if (layoutId() != View.NO_ID) {
            setContentView(layoutId())
        }
        mActivity = this
        mContext = this
        mBundle = intent.extras ?: Bundle()
        mDialogManager = EasyDialogManager(this)
        onBundle()
        init(savedInstanceState)
        initView()
        initData()
    }

    protected open fun preCreate(savedInstanceState: Bundle?) {
    }

    protected open fun onBundle() {
    }

    protected open fun init(savedInstanceState: Bundle?) {
    }

    abstract fun layoutId(): Int

    abstract fun initView()

    abstract fun initData()

    override fun showToast(msg: String?) {
        ToastUtil.instance(mContext).showToast(msg)
    }

    override fun showNextActivity(cls: Class<*>, bundle: Bundle?) {
        ActivityUtils.showNextActivity(mActivity, cls, bundle)
    }

    override fun toNextActivity(cls: Class<*>, bundle: Bundle?) {
        ActivityUtils.toNextActivity(mActivity, cls, bundle)
    }

    override fun showEasyDialog(dialog: BaseDialogFragment) {
        mDialogManager.showDialog(dialog)
    }

    override fun dismissEasyDialog(dialog: BaseDialogFragment) {
        mDialogManager.dismissDialog(dialog)
    }

    override fun dismissAllEasyDialog() {
        mDialogManager.clearDialogs()
    }

    override fun onDestroy() {
        AppManager.instance.removeActivity(this)
        dismissAllEasyDialog()
        super.onDestroy()
    }
}