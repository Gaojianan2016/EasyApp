package com.gjn.easyapp.easybase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseDatabindingFragment<VDB : ViewDataBinding> : BaseLazyFragment() {

    protected lateinit var dataBinding: VDB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        dataBinding.lifecycleOwner = this
        return dataBinding.root
    }
}