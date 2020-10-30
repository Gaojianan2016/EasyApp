package com.gjn.easyapp.easybase

abstract class BaseLazyFragment : ABaseFragment() {
    private var isFirstVisible = true
    private var isPrepared = false

    override fun initData() {
        initPrepare()
    }

    override fun onResume() {
        super.onResume()
        if (isFirstVisible) {
            initPrepare()
            isFirstVisible = false
        } else {
            againUpdateData()
        }
    }

    private fun initPrepare() {
        if (isPrepared) {
            lazyData()
        } else {
            isPrepared = true
        }
    }

    protected open fun againUpdateData() {}

    protected abstract fun lazyData()
}