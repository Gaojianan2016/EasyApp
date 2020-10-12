package com.gjn.easyapp

import android.view.View
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easydialoger.EasyDialogUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ABaseActivity() {

    var size = -1

    private val mEasyDialogUtils = EasyDialogUtils

    override fun layoutId(): Int = R.layout.activity_main

    override fun init() {
        super.init()
        EasyDialogUtils.instance(mActivity)
    }

    override fun initView() {
        tv_test.setOnClickListener{
//            size++
//            showToast("测试 $size")

//            mEasyDialogUtils.showAndroidDialog("标题", "测试")
//            mEasyDialogUtils.showEasyNormalDialog("消息",
//                positive = "按钮1",
//                positiveClickListener = View.OnClickListener { showToast("关闭按钮1") }
//            )
//            mEasyDialogUtils.showSmallLoadingDialog()
        }
    }

    override fun initData() {

    }
}