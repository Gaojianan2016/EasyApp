package com.gjn.easyapp

import android.view.View
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easybase.BaseLog
import com.gjn.easyapp.easydialoger.EasyDialogUtils
import com.gjn.easyapp.easyutils.heightPixels
import com.gjn.easyapp.easyutils.togglePassword
import com.gjn.easyapp.easyutils.widthPixels
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ABaseActivity() {

    var size = -1

    private val mEasyDialogUtils = EasyDialogUtils

    override fun layoutId(): Int = R.layout.activity_main

    override fun init() {
        super.init()
        EasyDialogUtils.instance(mActivity)
        BaseLog.isDebug = true
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

//            BaseLog.i("测试")
//            BaseLog.i("测试2")
//            BaseLog.i("测试3")

//            et_test.togglePassword()

//            BaseLog.i("屏幕宽度 ${mContext.widthPixels()} 屏幕高度 ${mContext.heightPixels()}")
        }
    }

    override fun initData() {

    }
}