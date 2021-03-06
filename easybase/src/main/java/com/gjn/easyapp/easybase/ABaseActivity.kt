package com.gjn.easyapp.easybase

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.gjn.easyapp.easydialoger.EasyDialogManager
import com.gjn.easyapp.easydialoger.base.BaseDialogFragment
import com.gjn.easyapp.easytoaster.ToastUtil
import com.gjn.easyapp.easyutils.AppManager
import com.gjn.easyapp.easyutils.createAndroidViewModel
import com.gjn.easyapp.easyutils.createViewModel
import com.gjn.easyapp.easyutils.startActivity

abstract class ABaseActivity : AppCompatActivity(), UIEvent {

    protected lateinit var mActivity: AppCompatActivity
    protected lateinit var mContext: Context
    protected lateinit var mBundle: Bundle
    protected lateinit var mDialogManager: EasyDialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        AppManager.instance.addActivity(this)
        preCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        mActivity = this
        mContext = this
        mBundle = intent.extras ?: Bundle()
        mDialogManager = EasyDialogManager(this)
        bindContentView()
        onBundle()
        init(savedInstanceState)
        initView()
        initData()
    }

    protected open fun preCreate(savedInstanceState: Bundle?) {
    }

    protected open fun bindContentView() {
        if (layoutId() != View.NO_ID) {
            setContentView(layoutId())
        }
    }

    protected open fun onBundle() {
    }

    protected open fun init(savedInstanceState: Bundle?) {
    }

    abstract fun layoutId(): Int

    abstract fun initView()

    abstract fun initData()

    override fun showToast(msg: String?) {
        ToastUtil.showToast(msg)
    }

    override fun showNextActivity(cls: Class<out Activity>, bundle: Bundle?) {
        cls.startActivity(mActivity, bundle)
    }

    override fun toNextActivity(cls: Class<out Activity>, bundle: Bundle?) {
        showNextActivity(cls, bundle)
        finish()
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

    protected inline fun <reified T : ViewModel> bindViewModel(
        clz: Class<T>
    ): Lazy<T> = lazy { clz.createViewModel(this) }

    protected inline fun <reified T : ViewModel> bindAndroidViewModel(
        clz: Class<T>
    ): Lazy<T> = lazy { clz.createAndroidViewModel(this, mActivity.application) }
}