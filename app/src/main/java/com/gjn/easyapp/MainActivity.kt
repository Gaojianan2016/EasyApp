package com.gjn.easyapp

import android.graphics.BitmapFactory
import android.graphics.Color
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easybase.BaseLog
import com.gjn.easyapp.easydialoger.EasyDialogUtils
import com.gjn.easyapp.easyutils.*
import com.google.zxing.EncodeHintType
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

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

//            val enMap = QRCodeUtils.defaultEncodeMap
//            enMap[EncodeHintType.MARGIN] = 1
//            val qrBmp = QRCodeUtils.stringEncode("你好，我是二维码",
//                positiveColor = Color.RED, negativeColor = Color.YELLOW, hints = enMap,
//                logoBitmap = BitmapFactory.decodeResource(resources, R.mipmap.balance_bg),
//                scale = 0.1f
//            )
//
//            iv_code_test.setImageBitmap(qrBmp)
//            tv_test.text = QRCodeUtils.bitmapDecode(qrBmp)

            val d = 35.7
            println("${d.decimalFormat()} , ${d.decimalFormat(prefix = "￥")}, ${d.decimalFormat(suffix = "MB")}")

            val s = "我是一段中文"
            val s2 = "我has中文"
            val s3 = "easdasdk.213123"
            val s4 = "easdasdk。213123"
            println("s = ${s.isChinese()} s2 = ${s2.hasChinese()} s3 = ${s3.hasChinese()} s4 = ${s4.hasChinese()}")

            println(s.toMd5())

            println(s3.hidePhone())

            val data1 = 2 * 1000
            val data2 = 3 * MINUTE * 1000
            val data3 = 2 * HOUR * 1000
            val data4 = DAY * 1000
            val data5 = 6 * DAY * 1000
            val data6 = 15 * DAY * 1000
            val data7 = 2 * DAY * 1000
            val data8 = 377 * DAY * 1000
            println("${data1.toLong().elapsedTime()} , ${data2.elapsedTime()}  , ${data3.elapsedTime()}  " +
                    ", ${data4.elapsedTime()}  , ${data5.elapsedTime()}  , ${data6.elapsedTime()} " +
                    ", ${data7.elapsedTime()} , ${data8.elapsedTime()} ")

            val time1 = 0
            val time2 = 10 * 1000
            val time3 = 5 * 60 * 1000
            val time4 = 3 * 24 * 60 * 1000

            println("${time1.toLong().toSecondFormat()} , ${time2.toLong().toSecondFormat()} " +
                    ", ${time3.toLong().toSecondFormat()} , ${time4.toLong().toSecondFormat()} ")
        }
    }

    override fun initData() {

    }
}