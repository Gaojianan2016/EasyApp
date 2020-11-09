package com.gjn.easyapp.base

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gjn.easyapp.easyutils.ResourcesUtils
import com.gjn.easyapp.easyutils.click
import com.gjn.easyapp.easyutils.clickLong
import com.gjn.easyapp.easyutils.intervalOpen

abstract class BaseKtRecyclerAdapter<T>(
    val context: Context,
    protected val layoutId: Int,
    _data: MutableList<T> = mutableListOf()
) : RecyclerView.Adapter<BaseVH>(), AdapterItemEvent<T> {

    val activity: Activity = context as Activity
    var data: MutableList<T> = _data
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClickListener: OnItemClickListener<T>? = null
    var onItemLongClickListener: OnItemLongClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        val vh = BaseVH(ResourcesUtils.inflate(context, layoutId, parent)!!)
        initVH(vh)
        addItemClick(vh)
        return vh
    }

    protected open fun initVH(vh: BaseVH) {
    }

    private fun addItemClick(vh: BaseVH) {
        onItemClickListener?.let {
            vh.itemView.click {
                val position = vh.layoutPosition
                it.onClick(this, data[position], position)
            }
        }
        onItemLongClickListener?.let {
            vh.itemView.clickLong {
                val position = vh.layoutPosition
                it.onLongClick(this, data[position], position)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BaseVH, position: Int) {
        convertData(holder, data[position], position)
    }

    abstract fun convertData(holder: BaseVH, item: T, position: Int)

    override fun add(item: T?) {
        add(data.size, item)
    }

    override fun add(data: MutableList<T>?) {
        data?.forEach {
            add(it)
        }
    }

    override fun add(position: Int, item: T?) {
        if (item == null) return
        data.add(position.intervalOpen(0, data.size), item)
        notifyItemInserted(position.intervalOpen(0, data.size))
    }

    override fun delete(position: Int) {
        data.removeAt(position)
        notifyDataSetChanged()
    }

    override fun delete(item: T?) {
        if (item == null) return
        data.remove(item)
        notifyDataSetChanged()
    }

    override fun change(position: Int, item: T?) {
        if (item == null) return
        data[position] = item
        notifyItemChanged(position)
    }

    override fun move(from: Int, to: Int) {
        val temp: T = data[from]
        data.removeAt(from)
        data.add(to, temp)
        notifyItemMoved(from, to)
    }

    override fun clear() {
        data.clear()
    }

    interface OnItemClickListener<T> {
        fun onClick(view: View, item: T, position: Int)
    }

    interface OnItemLongClickListener<T> {
        fun onLongClick(view: View, item: T, position: Int): Boolean
    }
}