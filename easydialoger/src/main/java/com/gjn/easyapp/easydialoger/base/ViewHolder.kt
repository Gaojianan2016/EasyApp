package com.gjn.easyapp.easydialoger.base

import android.content.Context
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.gjn.easyapp.easyutils.ResourcesUtils

class ViewHolder private constructor(var view: View?) {

    private val views: SparseArray<View> = SparseArray()

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

        fun create(
            context: Context?,
            @LayoutRes resource: Int,
            root: ViewGroup?
        ): ViewHolder = create(ResourcesUtils.inflate(context, resource, root))
    }
}