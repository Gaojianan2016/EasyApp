package com.gjn.easyapp.easybase

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.gjn.easyapp.easydialoger.EasyDialogManager
import com.gjn.easyapp.easydialoger.base.BaseDialogFragment
import com.gjn.easyapp.easytoaster.ToastUtil
import com.gjn.easyapp.easyutils.ActivityUtils

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
        ToastUtil.instance(mActivity).showToast(msg)
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

    override fun onDestroyView() {
        dismissAllEasyDialog()
        super.onDestroyView()
    }
}