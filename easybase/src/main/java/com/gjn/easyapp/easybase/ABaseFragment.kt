package com.gjn.easyapp.easybase

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.gjn.easyapp.easydialoger.EasyDialogManager
import com.gjn.easyapp.easydialoger.base.BaseDialogFragment
import com.gjn.easyapp.easytoaster.ToastUtil
import com.gjn.easyapp.easyutils.createAndroidViewModel
import com.gjn.easyapp.easyutils.createViewModel
import com.gjn.easyapp.easyutils.startActivity

abstract class ABaseFragment : Fragment(), UIEvent {

    protected lateinit var mFragment: Fragment
    protected lateinit var mActivity: FragmentActivity
    protected lateinit var mBundle: Bundle
    protected lateinit var mDialogManager: EasyDialogManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        preCreate()
        super.onCreate(savedInstanceState)
        mFragment = this
        mBundle = arguments ?: Bundle()
        mDialogManager = EasyDialogManager(this)
        onBundle()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (layoutId() != View.NO_ID) {
            inflater.inflate(layoutId(), container, false)
        } else {
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
        ToastUtil.showToast(msg)
    }

    override fun showNextActivity(cls: Class<out Activity>, bundle: Bundle?) {
        cls.startActivity(mActivity, bundle)
    }

    override fun toNextActivity(cls: Class<out Activity>, bundle: Bundle?) {
        showNextActivity(cls, bundle)
        mActivity.finish()
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

    override fun onDestroyView() {
        dismissAllEasyDialog()
        super.onDestroyView()
    }

    protected inline fun <reified T : ViewModel> bindViewModel(
        clz: Class<T>
    ): Lazy<T> = lazy { clz.createViewModel(this) }

    protected inline fun <reified T : ViewModel> bindAndroidViewModel(
        clz: Class<T>
    ): Lazy<T> = lazy { clz.createAndroidViewModel(this, mActivity.application) }
}