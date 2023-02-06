package com.gjn.easyapp

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Environment
import android.view.View
import androidx.core.app.ActivityCompat
import com.gjn.easyapp.databinding.DialogTestBinding
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
    var fileName = ""

    override fun layoutId(): Int = R.layout.fragment_a1

    override fun initView() {
        println("A1Fragment initView")
    }

    override fun lazyData() {
        println("A1Fragment lazyData")

        arrayOf(
            btn0, btn1, btn2, btn3, btn4, btn6, btn8,
            btn9, btn10, btn11, btn12, btn13, btn14
        ).setOnClickListeners(Click())

        btn5.click {
            println("屏幕宽度 ${mActivity.screenWidth} 屏幕高度 ${mActivity.screenHeight}")
            val d = 35.7
            println("${d.format()} , ${d.format(prefix = "￥")}, ${d.format(suffix = "MB")}")
            val s = "我是一段中文"
            val s2 = "我has中文"
            val s3 = "easdasdk.213123"
            val s4 = "easdasdk。213123"
            val s5 = "17745645645"
            println("s = ${s.isChinese()} s2 = ${s2.containsChinese()} s3 = ${s3.containsChinese()} s4 = ${s4.containsChinese()}")

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

            println("list isLimitSize 2 ${list.isLimitSize(2)}")
            println("list2 isLimitSize 10 ${list2.isLimitSize()}")

            println("12,7,8,sad,s4a, splitToList ${"12,7,8,sad,s4a".split2List(",")}")

            val json = "{\"code\":0,\"message\":\"操 作 成 功 ！\"}"
            println("json ${json.formatJson()}")
        }

        btn7.click {

        }

        btn15.click {
            mActivity.quickRequestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                showToast("获取成功")
            }
        }

        btn16.click {
            mActivity.quickActivityResult(
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
            fileName = mActivity.quickShoot(
                "${Environment.getExternalStorageDirectory()}/Test/11.png".toFile()
            ) { _, _ ->
                val file = fileName.toFile()
                if (file.exists()) {
                    println("$fileName 文件存在")
                } else {
                    println("$fileName 文件不存在")
                }
            }
        }

        btn18.click {

        }

        btn19.click {

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
//                    DemoActivity::class.java.startActivity(mActivity)
                }
                btn1 -> {
                    size++
                    showToast("测试 $size")
                }
                btn2 -> {
                    mDialogManager.showAndroidDialog("标题", "测试")
                    mDialogManager.showEasyNormalDialog("消息",
                        positive = "按钮1",
                        positiveClickBlock = { showToast("关闭按钮1") }
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
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                btn3 -> {
                    et_pwd.togglePasswordVisible()
                }
                btn4 -> {
                    val bmp = QRCodeUtils.stringEncode("你好，我是正常二维码")
                    println("解码 = ${QRCodeUtils.bitmapDecode(bmp)}")

//                    iv_qrcode.setQrCodeBitmap(
//                        "你好，我是有图标的二维码",
//                        positiveColor = Color.RED,
//                        negativeColor = Color.YELLOW,
//                        logoBitmap = BitmapFactory.decodeResource(resources, R.mipmap.balance_bg),
//                        scale = 0.1f
//                    )
                }
                btn6 -> {
                    println("是否连接网络 ${mActivity.isNetworkConnected()}")
                    println("是否连接wifi ${mActivity.isWifiConnected()}")
                    println("是否连接流量 ${mActivity.isMobileConnected()}")
                }
                btn8 -> {
                    val drawable = ActivityCompat.getDrawable(mActivity, R.mipmap.icon_bargain)

                    val sp1 = "我是一段测试文字(Has En)".createImageSpannableStringBuilder(
                        "1 ",
                        imageSpan = CenterAlignImageSpan(drawable!!)
                    )
                    val sp2 = sp1.matcherTextToColor(Color.RED, arrayOf("测试", "(H"))
                    tv_wz.run {
                        text = sp2
                        textSize = 24f
                    }

                    val sp3 = "我是一段测试文字(Has En)".createImageSpannableStringBuilder(
                        "1 ",
                        drawable = drawable
                    )
                    val sp4 = sp3.matcherTextToColor(Color.BLUE, arrayOf("文字", "n)"))
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
//                    showNextActivity(GalleryActivity::class.java, Bundle().apply {
//                        putString("msg", "首页发送的数据")
//                    })
                }
                btn13 -> {
                    val status = mActivity.statusBarHeight
                    val navigation = mActivity.navigationBarHeight
                    println("状态栏 ${status}px -> ${status.px2dp()}dp")
                    println("底边栏 ${navigation}px -> ${navigation.px2dp()}dp")
                }
                btn14 -> {
                    val result =
                        mActivity.checkPermissionExistManifest(Manifest.permission.READ_EXTERNAL_STORAGE)
                    println("是否含有权限 $result")
                }
            }
        }
    }

}