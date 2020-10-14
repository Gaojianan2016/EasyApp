package com.gjn.easyapp

import android.graphics.BitmapFactory
import android.graphics.Color
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easybase.BaseLog
import com.gjn.easyapp.easydialoger.EasyDialogUtils
import com.gjn.easyapp.easyutils.QRCodeUtils
import com.google.zxing.EncodeHintType
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

//            val bmp = QRCodeUtils.stringEncode("你好，我是二维码")
//            iv_code_test.setImageBitmap(bmp)
//            tv_test.text = QRCodeUtils.bitmapDecode(bmp)

            val enMap = QRCodeUtils.defaultEncodeMap
            enMap[EncodeHintType.MARGIN] = 1
            val qrBmp = QRCodeUtils.stringEncode("你好，我是二维码",
                positiveColor = Color.RED, negativeColor = Color.YELLOW, hints = enMap,
                logoBitmap = BitmapFactory.decodeResource(resources, R.mipmap.balance_bg),
                scale = 0.1f
            )

            iv_code_test.setImageBitmap(qrBmp)
            tv_test.text = QRCodeUtils.bitmapDecode(qrBmp)

        }
    }

    override fun initData() {

    }
}