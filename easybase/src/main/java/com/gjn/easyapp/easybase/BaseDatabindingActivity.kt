package com.gjn.easyapp.easybase

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseDatabindingActivity<VDB : ViewDataBinding> : ABaseActivity() {

    protected lateinit var dataBinding: VDB

    override fun bindContentView() {
        dataBinding = DataBindingUtil.setContentView(mActivity, layoutId())
        dataBinding.lifecycleOwner = this
    }
}