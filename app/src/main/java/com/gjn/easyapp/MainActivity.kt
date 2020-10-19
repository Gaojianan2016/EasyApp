package com.gjn.easyapp

import android.graphics.Color
import androidx.core.app.ActivityCompat
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easybase.BaseLog
import com.gjn.easyapp.easydialoger.EasyDialogUtils
import com.gjn.easyapp.easyutils.StringUtils
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
        tv_test.setOnClickListener {
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

//            val d = 35.7
//            println("${d.decimalFormat()} , ${d.decimalFormat(prefix = "￥")}, ${d.decimalFormat(suffix = "MB")}")
//            val s = "我是一段中文"
//            val s2 = "我has中文"
//            val s3 = "easdasdk.213123"
//            val s4 = "easdasdk。213123"
//            val s5 = "17745645645"
//            println("s = ${s.isChinese()} s2 = ${s2.hasChinese()} s3 = ${s3.hasChinese()} s4 = ${s4.hasChinese()}")
//
//            println(s.toMd5())
//
//            println(s5.hidePhone())

//            val data1 = 2 * 1000
//            val data2 = 3 * MINUTE * 1000
//            val data3 = 2 * HOUR * 1000
//            val data4 = DAY * 1000
//            val data5 = 6 * DAY * 1000
//            val data6 = 15 * DAY * 1000
//            val data7 = 2 * DAY * 1000
//            val data8 = 377 * DAY * 1000
//            println("${data1.toLong().elapsedTime()} , ${data2.elapsedTime()}  , ${data3.elapsedTime()}  " +
//                    ", ${data4.elapsedTime()}  , ${data5.elapsedTime()}  , ${data6.elapsedTime()} " +
//                    ", ${data7.elapsedTime()} , ${data8.elapsedTime()} ")
//
//            val time1 = 0
//            val time2 = 10 * 1000
//            val time3 = 5 * 60 * 1000
//            val time4 = 3 * 24 * 60 * 1000
//
//            println("${time1.toLong().toSecondFormat()} , ${time2.toLong().toSecondFormat()} " +
//                    ", ${time3.toLong().toSecondFormat()} , ${time4.toLong().toSecondFormat()} ")

//            println("是否连接网络 ${mContext.isNetworkConnected()}")
//            println("是否连接wifi ${mContext.isWifiConnected()}")
//            println("是否连接流量 ${mContext.isMobileConnected()}")

//            //2020 元旦
//            val day0 = StringUtils.createGregorianCalendar(2020).timeInMillis
//
//            //2020 10 19 14 17 20
//            val day1 = StringUtils.createGregorianCalendar(2020, month = 9, date = 19, hourOfDay = 14, minute = 17, second = 20).timeInMillis
//            //2020 10 19 10 00 00
//            val day2 = StringUtils.createGregorianCalendar(2020, month = 9, date = 19, hourOfDay = 10).timeInMillis
//            //2020 10 18 14 00 00
//            val day3 = StringUtils.createGregorianCalendar(2020, month = 9, date = 18, hourOfDay = 14).timeInMillis
//            //2020 10 11 00 00 00
//            val day4 = StringUtils.createGregorianCalendar(2020, month = 9, date = 11).timeInMillis
//            //2020 7 1 00 00 00
//            val day5 = StringUtils.createGregorianCalendar(2020, month = 6, date = 1).timeInMillis
//
//            val day55 = System.currentTimeMillis() - 3000
//            val day555 = System.currentTimeMillis() - 10000
//
//            //2019 12 22 00 00 00
//            val day6 = StringUtils.createGregorianCalendar(2019, month = 11, date = 22).timeInMillis
//            //2019 9 22 00 00 00
//            val day7 = StringUtils.createGregorianCalendar(2019, month = 8, date = 22).timeInMillis
//
//            println("day1 ${StringUtils.elapsedTime(time = day1)}")
//            println("day2 ${StringUtils.elapsedTime(time = day2)}")
//            println("day3 ${StringUtils.elapsedTime(time = day3)}")
//            println("day4 ${StringUtils.elapsedTime(time = day4)}")
//            println("day5 ${StringUtils.elapsedTime(time = day5)}")
//            println("day55 ${StringUtils.elapsedTime(time = day55)}")
//            println("day555 ${StringUtils.elapsedTime(time = day555)}")
//            println("day6 ${StringUtils.elapsedTime(time = day6)}")
//            println("day7 ${StringUtils.elapsedTime(time = day7)}")
//            println("----------------------------------------")
//            println("day6 ${StringUtils.elapsedTime(day0, day6)}")
//            println("day7 ${StringUtils.elapsedTime(day0, day7)}")

            val drawable = ActivityCompat.getDrawable(mContext, R.mipmap.icon_bargain)

            val sp1 = StringUtils.matcherDrawableSpan(
                "1 ", "我是一段测试文字(Has En)",
                imageSpan = CenterAlignImageSpan(drawable)
            )
            val sp2 = StringUtils.matcherColorSpan(sp1, Color.RED, "测试", "(H", "")
            tv_test.text = sp2
            tv_test.textSize = 18f

            val sp3 = StringUtils.matcherDrawableSpan(
                "1 ", "我是一段测试文字(Has En)",
                drawable = drawable
            )
            val sp4 = StringUtils.matcherColorSpan(sp3, Color.BLUE, "文字", "n)")

            tv_test2.text = sp4
        }
    }

    override fun initData() {

    }
}