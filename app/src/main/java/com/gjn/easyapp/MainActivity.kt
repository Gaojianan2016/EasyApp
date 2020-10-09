package com.gjn.easyapp

import com.gjn.easyapp.easybase.ABaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ABaseActivity() {

    var size = -1

    override fun layoutId(): Int = R.layout.activity_main

    override fun initView() {
        tv_test.setOnClickListener{
            size++
            showToast("测试 $size")
        }
    }

    override fun initData() {

    }
}