package com.gjn.easyapp.easydialoger.base

import android.content.Context
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.gjn.easyapp.easyutils.inflate
import com.gjn.easyapp.easyutils.inflateDataBindingUtil

class ViewHolder private constructor(var view: View?) {

    private val views: SparseArray<View> = SparseArray()

    @Suppress("UNCHECKED_CAST")
    fun <T : View?> findViewById(id: Int): T? {
        var idView = views[id]
        if (idView == null) {
            idView = view!!.findViewById(id)
            views.put(id, idView)
        }
        return idView as T
    }

    companion object {
        fun create(view: View?): ViewHolder = ViewHolder(view)

        fun create(context: Context?, @LayoutRes resource: Int, root: ViewGroup?): ViewHolder =
            create(context.inflate(resource, root, false))
    }
}

class DataBindingHolder private constructor(var dataBinding: ViewDataBinding) {

    companion object {
        fun create(dataBinding: ViewDataBinding): DataBindingHolder = DataBindingHolder(dataBinding)

        fun create(context: Context?, @LayoutRes resource: Int, root: ViewGroup?): DataBindingHolder {
            val binding: ViewDataBinding = context.inflateDataBindingUtil(resource, root, false)
            return create(binding)
        }
    }
}