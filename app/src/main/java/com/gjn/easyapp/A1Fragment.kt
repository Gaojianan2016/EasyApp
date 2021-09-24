package com.gjn.easyapp

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.core.app.ActivityCompat
import com.gjn.easyapp.databinding.DialogTestBinding
import com.gjn.easyapp.demo.DemoActivity
import com.gjn.easyapp.easybase.BaseLazyFragment
import com.gjn.easyapp.easydialoger.base.simpleDataBindingDialog
import com.gjn.easyapp.easynetworker.DownLoadManager
import com.gjn.easyapp.easynetworker.SimpleDownLoadListener
import com.gjn.easyapp.easyutils.*
import kotlinx.android.synthetic.main.fragment_a1.*
import kotlinx.coroutines.delay
import okhttp3.Call
import java.io.File

class A1Fragment : BaseLazyFragment() {

    var size = -1
    var name by MySP("name", "")
    var age by MySP("age", -1)
    var fileName = ""

    override fun layoutId(): Int = R.layout.fragment_a1

    override fun initView() {
        println("A1Fragment initView")
        StatusBarUtil.setContentViewFitsSystemWindows(mActivity, true)
    }

    override fun lazyData() {
        println("A1Fragment lazyData")

        setOnClickListeners(
            btn0, btn1, btn2, btn3,
            btn4, btn6, btn8, btn9,
            btn10, btn11, btn12, btn13,
            btn14, listener = Click()
        )

        btn5.click {
            println("屏幕宽度 ${mActivity.screenWidth()} 屏幕高度 ${mActivity.screenHeight()}")
            val d = 35.7
            println(
                "${d.decimalFormat()} , ${d.decimalFormat(prefix = "￥")}, ${
                    d.decimalFormat(
                        suffix = "MB"
                    )
                }"
            )
            val s = "我是一段中文"
            val s2 = "我has中文"
            val s3 = "easdasdk.213123"
            val s4 = "easdasdk。213123"
            val s5 = "17745645645"
            println("s = ${s.isChinese()} s2 = ${s2.hasChinese()} s3 = ${s3.hasChinese()} s4 = ${s4.hasChinese()}")

            println(s.toMd5())

            println(s5.hidePhone())

            println("1-5 ${7.intervalOpen(1, 5)}")
            println("1f-5f ${0f.intervalOpen(1f, 5f)}")
            println("1-10 ${8.intervalOpen(1, 10)}")

            val list = arrayListOf("1", "2")
            val list2 = mutableListOf("2")
            val list3: ArrayList<String>? = null
            val list4: MutableList<String>? = null

            println("list isEmpty ${list.isEmpty()}")
            println("list2 isEmpty ${list2.isEmpty()}")
            println("list3 isEmpty ${list3.isNullOrEmpty()}")
            println("list4 isEmpty ${list4.isNullOrEmpty()}")

            println("list isList ${list.isList()}")
            println("list2 isList ${list2.isList()}")
            println("list3 isList ${list3?.isList()}")
            println("list4 isList ${list4?.isList()}")

            println("list insufficientSize 2 ${list.insufficientSize(2)}")
            println("list2 insufficientSize 10 ${list2.insufficientSize()}")

            println("12,7,8,sad,s4a, splitToList ${"12,7,8,sad,s4a".split2List(",")}")

            println("name = $name age = $age")
            if (name == "张三") {
                name = "李四"
                age = 26
            } else {
                name = "张三"
                age = 18
            }
            println("new name = $name new age = $age")
            val json = "{\"code\":0,\"message\":\"操 作 成 功 ！\"}"
            println("json ${JsonUtil.formatJson(json)}")
        }

        btn7.click {
            val data1 = 2 * 1000
            val data2 = 3 * MINUTE * 1000
            val data3 = 2 * HOUR * 1000
            val data4 = DAY * 1000
            val data5 = 6 * DAY * 1000
            val data6 = 15 * DAY * 1000
            val data7 = 2 * DAY * 1000
            val data8 = 377 * DAY * 1000
            println(
                "${
                    data1.toLong()
                        .elapsedTime()
                } , ${data2.elapsedTime()}  , ${data3.elapsedTime()}  " +
                        ", ${data4.elapsedTime()}  , ${data5.elapsedTime()}  , ${data6.elapsedTime()} " +
                        ", ${data7.elapsedTime()} , ${data8.elapsedTime()} "
            )

            val time1 = 0
            val time2 = 10 * 1000
            val time3 = 5 * 60 * 1000
            val time4 = 3 * 24 * 60 * 1000

            println(
                "${time1.toLong().toSecondFormat()} , ${time2.toLong().toSecondFormat()} " +
                        ", ${time3.toLong().toSecondFormat()} , ${time4.toLong().toSecondFormat()} "
            )

            //2020 元旦
            val day0 = StringUtils.createGregorianCalendar(2020).timeInMillis

            //2020 10 19 14 17 20
            val day1 = StringUtils.createGregorianCalendar(
                2020,
                month = 9,
                date = 19,
                hourOfDay = 14,
                minute = 17,
                second = 20
            ).timeInMillis
            //2020 10 19 10 00 00
            val day2 = StringUtils.createGregorianCalendar(
                2020,
                month = 9,
                date = 19,
                hourOfDay = 10
            ).timeInMillis
            //2020 10 18 14 00 00
            val day3 = StringUtils.createGregorianCalendar(
                2020,
                month = 9,
                date = 18,
                hourOfDay = 14
            ).timeInMillis
            //2020 10 11 00 00 00
            val day4 = StringUtils.createGregorianCalendar(2020, month = 9, date = 11).timeInMillis
            //2020 7 1 00 00 00
            val day5 = StringUtils.createGregorianCalendar(2020, month = 6, date = 1).timeInMillis

            val day55 = System.currentTimeMillis() - 3000
            val day555 = System.currentTimeMillis() - 10000

            //2019 12 22 00 00 00
            val day6 = StringUtils.createGregorianCalendar(2019, month = 11, date = 22).timeInMillis
            //2019 9 22 00 00 00
            val day7 = StringUtils.createGregorianCalendar(2019, month = 8, date = 22).timeInMillis

            println("day1 ${StringUtils.elapsedTime(time = day1)}")
            println("day2 ${StringUtils.elapsedTime(time = day2)}")
            println("day3 ${StringUtils.elapsedTime(time = day3)}")
            println("day4 ${StringUtils.elapsedTime(time = day4)}")
            println("day5 ${StringUtils.elapsedTime(time = day5)}")
            println("day55 ${StringUtils.elapsedTime(time = day55)}")
            println("day555 ${StringUtils.elapsedTime(time = day555)}")
            println("day6 ${StringUtils.elapsedTime(time = day6)}")
            println("day7 ${StringUtils.elapsedTime(time = day7)}")
            println("----------------------------------------")
            println("day6 ${StringUtils.elapsedTime(day0, day6)}")
            println("day7 ${StringUtils.elapsedTime(day0, day7)}")
        }

        btn15.click {
            mActivity.simpleRequestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                showToast("获取成功")
            }
        }

        btn16.click {
            mActivity.simpleActivityResult(
                Intent(
                    mActivity,
                    ActivityResultActivity::class.java
                )
            ) { code, data ->
                println("code $code data $data")
                data?.run {
                    val result = getStringExtra("msg")
                    showToast("获取到结果 $result")
                }
            }
        }

        btn17.click {
            fileName = mActivity.quickPhotography(
                "${Environment.getExternalStorageDirectory()}/Test/".file()
            ) { code, data ->
                val file = fileName.file()
                if (file.exists()) {
                    println("$fileName 文件存在")
                } else {
                    println("$fileName 文件不存在")
                }
            }
        }

        btn18.click {
//            StatusBarUtil.statusBarMode(mActivity, true)
            StatusBarUtil.statusBarMode(
                mActivity, true,
                BitmapDrawable(
                    resources,
                    BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
                )
            )
        }

        btn19.click {
            var color = Color.RED
            when {
                size % 2 == 0 -> {
                    color = Color.YELLOW
                }
                size % 3 == 0 -> {
                    color = Color.BLUE
                }
                size % 5 == 0 -> {
                    color = Color.BLACK
                }
            }
            StatusBarUtil.statusBarMode(mActivity, colorOrDrawable = color)
        }
        btn20.click {

            val downLoadManager = DownLoadManager(mActivity)
            downLoadManager.onDownLoadListener = object : SimpleDownLoadListener() {
                override fun start(call: Call, file: File, name: String, length: Int) {
                    showToast("准备下载 $file")
                }

                override fun downLoading(call: Call, readStream: Int, totalStream: Int) {
                    println("进度 $readStream/$totalStream")
                }

                override fun success(call: Call, file: File) {
                    showToast("下载成功")
                    downLoadManager.openFile(file)
                }

                override fun fail(call: Call) {
                    showToast("下载失败")
                }

                override fun error(call: Call, tr: Throwable) {
                    tr.printStackTrace()
                }
            }
            downLoadManager.downLoadFile(
                "http://static.yunchou2020.com/pro/apk/yunchou.apk",
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
//                , "app111.apk"
            )
        }
    }

    inner class Click : View.OnClickListener {

        override fun onClick(v: View?) {
            when (v) {
                btn0 -> {
                    DemoActivity::class.java.startActivity(mActivity)
                }
                btn1 -> {
                    size++
                    showToast("测试 $size")
                }
                btn2 -> {
                    mDialogManager.showAndroidDialog("标题", "测试")
                    mDialogManager.showEasyNormalDialog("消息",
                        positive = "按钮1",
                        positiveClickListener = { showToast("关闭按钮1") }
                    )
                    mDialogManager.showSmallLoadingDialog()

                    mDialogManager.showDialog(
                        simpleDataBindingDialog<DialogTestBinding>(R.layout.dialog_test) { _, _ -> }
                            .apply {
                                isTransparent = true
                                isMatchWidth = true
                            }
                    )

                    launchMain {
                        delay(5000)
                        try {
                            mDialogManager.clearDialogs()
                        }catch (e: Exception){
                            e.printStackTrace()
                        }
                    }
                }
                btn3 -> {
                    et_pwd.togglePassword()
                }
                btn4 -> {
                    val bmp = QRCodeUtils.stringEncode("你好，我是正常二维码")
                    println("解码 = ${QRCodeUtils.bitmapDecode(bmp)}")

                    iv_qrcode.setQrCodeBitmap(
                        "你好，我是有图标的二维码",
                        margin = 1,
                        positiveColor = Color.RED,
                        negativeColor = Color.YELLOW,
                        logoBitmap = BitmapFactory.decodeResource(resources, R.mipmap.balance_bg),
                        scale = 0.1f
                    )
                }
                btn6 -> {
                    println("是否连接网络 ${mActivity.isNetworkConnected()}")
                    println("是否连接wifi ${mActivity.isWifiConnected()}")
                    println("是否连接流量 ${mActivity.isMobileConnected()}")
                }
                btn8 -> {
                    val drawable = ActivityCompat.getDrawable(mActivity, R.mipmap.icon_bargain)

                    val sp1 = StringUtils.matcherDrawableSpan(
                        "1 ", "我是一段测试文字(Has En)",
                        imageSpan = CenterAlignImageSpan(drawable!!)
                    )
                    val sp2 = StringUtils.matcherColorSpan(sp1, Color.RED, "测试", "(H")
                    tv_wz.run {
                        text = sp2
                        textSize = 24f
                    }

                    val sp3 = StringUtils.matcherDrawableSpan(
                        "1 ", "我是一段测试文字(Has En)",
                        drawable = drawable
                    )
                    val sp4 = StringUtils.matcherColorSpan(sp3, Color.BLUE, "文字", "n)")
                    tv_wz2.text = sp4
                }
                btn9 -> {
                    val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.test_img)
                    iv_bitmap.setImageBitmap(bitmap.fastBlur(mActivity))
                }
                btn10 -> {
//                    mActivity.openQQ()
                    mActivity.openApp("com.tencent.tmgp.sgame")
                }
                btn11 -> {
                    iv_icon.setImageDrawable(mActivity.getAppIcon())
                    iv_icon2.setImageDrawable(mActivity.getAppIcon(QQ_PACKAGE_NAME))
                    iv_icon3.setImageDrawable(mActivity.getAppIcon(WECHAT_PACKAGE_NAME))
                }
                btn12 -> {
                    showNextActivity(GalleryActivity::class.java, Bundle().apply {
                        putString("msg", "首页发送的数据")
                    })
                }
                btn13 -> {
                    val status = mActivity.statusBarHeight()
                    val navigation = mActivity.navigationBarHeight()
                    println("状态栏 ${status}px -> ${status.px2dp(mActivity)}dp")
                    println("底边栏 ${navigation}px -> ${navigation.px2dp(mActivity)}dp")
                }
                btn14 -> {
                    val result = mActivity.hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    println("是否含有权限 $result")
                }
            }
        }
    }

}