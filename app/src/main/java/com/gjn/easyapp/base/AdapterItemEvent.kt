package com.gjn.easyapp.base

interface AdapterItemEvent<T> {

    fun add(item: T?)

    fun add(data: MutableList<T?>?)

    fun add(position: Int, item: T?)

    fun delete(position: Int)

    fun delete(item: T?)

    fun change(position: Int = 0, item: T?)

    fun move(from: Int, to: Int)

    fun clear()
}