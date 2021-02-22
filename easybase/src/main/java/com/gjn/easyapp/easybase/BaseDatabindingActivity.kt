package com.gjn.easyapp.easybase

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gjn.easyapp.easyutils.AutoScreenUtil

abstract class BaseDatabindingActivity<VDB : ViewDataBinding> : ABaseActivity() {

    protected lateinit var dataBinding: VDB

    override fun preCreate(savedInstanceState: Bundle?) {
        super.preCreate(savedInstanceState)
        AutoScreenUtil.setCustomDensity(this)
    }

    override fun bindContentView() {
        dataBinding = DataBindingUtil.setContentView(mActivity, layoutId())
        dataBinding.lifecycleOwner = this
    }
}