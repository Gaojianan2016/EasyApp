package com.gjn.easyapp.easydialoger.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

class DataBindingHolder private constructor(var dataBinding: ViewDataBinding) {

    companion object {
        fun create(dataBinding: ViewDataBinding): DataBindingHolder = DataBindingHolder(dataBinding)

        fun create(
            context: Context?,
            @LayoutRes resource: Int,
            root: ViewGroup?
        ): DataBindingHolder {
            val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                resource, root, false
            )
            return create(binding)
        }
    }
}