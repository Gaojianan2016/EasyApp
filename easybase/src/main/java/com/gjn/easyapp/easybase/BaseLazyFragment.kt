package com.gjn.easyapp.easybase

abstract class BaseLazyFragment : ABaseFragment(){
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
        }else{
            lazyData()
        }
    }

    private fun initPrepare() {
        if (isPrepared) {
            firstLazyData()
        }else{
            isPrepared = true
        }
    }

    protected abstract fun firstLazyData()

    protected abstract fun lazyData()
}