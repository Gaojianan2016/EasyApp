package com.gjn.easyapp

import com.gjn.easyapp.easybase.BaseLazyFragment

class A3Fragment : BaseLazyFragment() {

    override fun layoutId(): Int = R.layout.fragment_a3

    override fun initView() {
        println("A3Fragment initView")
    }

    override fun lazyData() {
        println("A3Fragment lazyData")
    }

    override fun againUpdateData() {
        println("A3Fragment againUpdateData")
    }

}