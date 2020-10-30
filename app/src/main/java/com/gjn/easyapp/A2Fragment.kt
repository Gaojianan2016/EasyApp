package com.gjn.easyapp

import com.gjn.easyapp.easybase.BaseLazyFragment

class A2Fragment: BaseLazyFragment() {

    override fun layoutId(): Int = R.layout.fragment_a2

    override fun initView() {
        println("A2Fragment initView")
    }

    override fun lazyData() {
        println("A2Fragment lazyData")
    }

}