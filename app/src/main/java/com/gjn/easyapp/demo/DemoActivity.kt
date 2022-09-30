package com.gjn.easyapp.demo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.gjn.easyapp.R
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easyutils.*
import kotlinx.android.synthetic.main.activity_demo.*
import kotlinx.coroutines.delay
import java.io.FileFilter
import java.util.*
import java.util.concurrent.TimeUnit

class DemoActivity : ABaseActivity(), NetworkStateManager.OnNetworkStateListener {

    override fun layoutId() = R.layout.activity_demo

    @SuppressLint("MissingPermission")
    override fun initView() {
        val bmp = mActivity.toBitmap(R.mipmap.test_img)

        btn1_ad.click {
            //id动画
//            mActivity.startActivity<ImageActivity>(enterResId = R.anim.anim_bottom_in, exitResId = R.anim.anim_bottom_out)

            //特殊动画
            mActivity.startActivity<ImageActivity>(options = makeSceneTransitionAnimationBundle()) {
//                putExtra(ImageActivity.DATA, DEFAULT_SCENE_TRANSITION_EXPLODE)
//                putExtra(ImageActivity.DATA, DEFAULT_SCENE_TRANSITION_SLIDE)
                putExtra(ImageActivity.DATA, DEFAULT_SCENE_TRANSITION_FADE)
            }
        }

        btn2_ad.click {
            mActivity.startActivity<ImageActivity>(sharedElements = arrayOf(btn1_ad, btn2_ad))
        }

        btn3_ad.click {
            mActivity.startActivity<ImageActivity>(
                sharedElements = arrayOf(btn3_ad),
                pairs = arrayOf(
                    ImageActivity.DATA to "数据4",
                    ImageActivity.ID to R.mipmap.ic_launcher
                )
            )
        }

        btn4_ad.click {
            showToast("是否打开软键盘 ${mActivity.isSoftInputVisible()}")
        }

        btn5_ad.click {
            mActivity.quickActivityResult(Intent(mActivity, ImageActivity::class.java))
            { code, data ->
                showToast("code=$code data=${data?.getStringExtra("data")}")
            }
        }

        btn6_ad.click {
            println("bmp old ${bmp?.width} * ${bmp?.height} = ${bmp?.toByte()?.size} ")
            val bmp2 = bmp?.scale(0.5f)
            println("newBmp new ${bmp2?.width} * ${bmp2?.height} = ${bmp2?.toByte()?.size} ")
            iv1_ad.setImageBitmap(bmp2)

//            val bmp3 = btn7_ad.toBitmap()
//            iv1_ad.setImageBitmap(bmp3)

//            val bmp4 = logo?.scale(0.5f)
//            iv1_ad.setImageBitmap(bmp4)

//            val bmp5 = bmp?.clip(0, 0, 50, 50)
//            iv1_ad.setImageBitmap(bmp5)

//            val bmp6 = bmp?.skew(5f, 0f)
//            iv1_ad.setImageBitmap(bmp6)

//            val bmp7 = bmp?.rotate(50f)
//            iv1_ad.setImageBitmap(bmp7)

//            val bmp8 = bmp?.alpha()
//            iv1_ad.setImageBitmap(bmp8)

//            val bmp9 = bmp?.gray()
//            iv1_ad.setImageBitmap(bmp9)
        }

        btn7_ad.click {

            val logo = mActivity.vectorToBitmap(R.drawable.ic_launcher_background)
            val bmp2 = bmp?.addImageWatermark(logo, 9, 0.5f, 88, -45f)
            iv1_ad.setImageBitmap(bmp2)

//            val bmp3 = bmp?.toCircle(5, Color.BLUE)
//            iv1_ad.setImageBitmap(bmp3)

//            val bmp4 = bmp?.toRoundCorner(10f, 5, Color.RED)
//            iv1_ad.setImageBitmap(bmp4)

//            val bmp5 = bmp?.addTextWatermark("水印\n12345", textSize = 40f, degrees = -45f, alpha = 188)
//            iv1_ad.setImageBitmap(bmp5)
        }

        btn8_ad.click {
            val bmp2 = bmp?.fastBlur(mActivity)
            iv1_ad.setImageBitmap(bmp2)
        }

        btn9_ad.click {
            val internalSize = mActivity.appInternalCacheSize()
            val externalSize = mActivity.appExternalCacheSize()
            val fileSize = mActivity.appFileSize()
            val sharedPrefsSize = mActivity.appSharedPrefsSize()
            val databasesSize = mActivity.appDatabasesSize()

            println(
                "internalSize = $internalSize\n" +
                        "externalSize = $externalSize\n" +
                        "fileSize = $fileSize\n" +
                        "sharedPrefsSize = $sharedPrefsSize\n" +
                        "databasesSize = $databasesSize\n"
            )
        }

        btn10_ad.click {
            mActivity.clearAppAllData()
            showToast("清理成功")
        }

        btn11_ad.click {
            mActivity.requestWRPermission {
                mActivity.installApp("/sdcard/Android/jsq.apk")
            }
        }

        btn12_ad.click {
            mActivity.uninstallApp("com.ddnapalon.calculator.gp")
        }

        btn13_ad.click {
            mActivity.openApp("com.ddnapalon.calculator.gp")
        }

        btn14_ad.click {
            mActivity.openAppDetailsSettings("com.ddnapalon.calculator.gp")
        }

        btn15_ad.click {
            iv2_ad.setImageDrawable(mActivity.getAppIcon())
            println("iconId ${mActivity.getAppIconId()}")
            println("applicationName ${mActivity.getApplicationName()}")
            println("appPath ${mActivity.getAppPath()}")
            println("versionName ${mActivity.getAppVersionName()}")
            println("versionCode ${mActivity.getAppVersionCode()}")
            println("signaturesSHA1 ${mActivity.getAppSignaturesSHA1()}")
            println("signaturesSHA256 ${mActivity.getAppSignaturesSHA256()}")
            println("signaturesMD5 ${mActivity.getAppSignaturesMD5()}")

            println("getAppLauncherClassName ${mActivity.getAppLauncherClassName(WECHAT_PACKAGE_NAME)}")
            println("getAppLauncherIntent ${mActivity.getAppLauncherIntent(WECHAT_PACKAGE_NAME)}")
        }

        btn16_ad.click {
            println("statusBarHeight ${mActivity.statusBarHeight}px")
            println("isStatusBarVisible ${mActivity.isStatusBarVisible}")
            println("isStatusBarLightMode ${mActivity.isStatusBarLightMode}")

            println("actionBarHeight ${mActivity.actionBarHeight}px")
            println("navigationBarHeight ${mActivity.navigationBarHeight}px")
            println("isNavBarVisible ${mActivity.isNavBarVisible}")
            println("isNavBarLightMode ${mActivity.isNavBarLightMode}")
        }

        btn17_ad.click {
//            //设置状态栏颜色
//            mActivity.setStatusBarColor(Color.TRANSPARENT, true)

//            //沉浸式状态栏
//            mActivity.setStatusBarColor(Color.TRANSPARENT)
//            //添加状态栏高度margin
//            nsv_ad.addMarginTopEqualStatusBarHeight()

//            //设置状态栏显示
//            mActivity.isStatusBarVisible = !mActivity.isStatusBarVisible
//            //设置状态栏模式
//            mActivity.isStatusBarLightMode = !mActivity.isStatusBarLightMode

            mActivity.startActivity<DrawerActivity>()
        }

        btn18_ad.click {
            println("isAutoBrightnessEnabled ${mActivity.isAutoBrightnessEnabled()}")
            println("screenBrightness ${mActivity.screenBrightness}")
            println("screenMaxBrightness ${mActivity.screenMaxBrightness}")
            println("windowBrightness ${window.windowBrightness}")
        }

        btn19_ad.click {
            window.setWindowBrightness(sb_ad.progress)
        }

        btn20_ad.debouncingClick {
            mActivity.copyClipboardText("我是复制文字")
        }

        btn21_ad.debouncingClick {
            println("getClipboardLabel ${mActivity.getClipboardLabel()}")
            println("getClipboardText ${mActivity.getClipboardText()}")
        }

        btn22_ad.debouncingClick {
            mActivity.clearClipboard()
        }

        btn23_ad.click {
            tv_color_ad.setBackgroundColor(randomColor())
        }

        btn24_ad.click {
            val color = randomColor()
            tv_color_ad.setBackgroundColor(color)
            println("${color.parseArgbColor()} isLightColor? ${color.isLightColor()}")
        }

        btn25_ad.click {
            var color = Color.BLACK
            tv_color_ad.setBackgroundColor(color)
            launch {
                delay(1000)
                color = color.changeColorAlpha(0.2f)
                tv_color_ad.setBackgroundColor(color)
                delay(1000)
                color = color.changeRedColorAlpha(0.3f)
                tv_color_ad.setBackgroundColor(color)
                delay(1000)
                color = color.changeBlueColorAlpha(0.4f)
                tv_color_ad.setBackgroundColor(color)
                delay(1000)
                color = color.changeGreenColorAlpha(0.5f)
                tv_color_ad.setBackgroundColor(color)
            }
        }

        btn26_ad.click {
            println("isDeviceRooted ${isDeviceRooted()}")
            println("isAdbEnabled ${mActivity.isAdbEnabled()}")
            println("sdkVersionName $sdkVersionName")
            println("sdkVersionCode $sdkVersionCode")
            println("androidID ${mActivity.androidID()}")
            println("getMacAddress ${mActivity.getMacAddress()}")
        }

        btn27_ad.click {
            val str = "我是一段测试数据"

            var encodeStr = str.urlEncode()
            println("urlEncode $encodeStr")
            println("urlDecode ${encodeStr.urlDecode()}")

            encodeStr = str.base64Encode2String()
            println("base64Encode $encodeStr")
            println("base64Decode ${String(encodeStr.base64Decode())}")

            encodeStr = str.binaryEncode()
            println("binaryEncode $encodeStr")
            println("binaryDecode ${encodeStr.binaryDecode()}")

            var encryptStr: String? = str.encryptMD5ToString()
            println("encryptMD5ToString $encryptStr")

            encryptStr = str.encryptHmacMD5ToString("123456".toByteArray())
            println("encryptHmacMD5ToString $encryptStr")
        }

        btn28_ad.click {
            val file =
                "${Environment.getExternalStorageDirectory().absolutePath}/aA_test/text.txt".toFile()

            println("file createOrExistsFile ${file.createOrExistsFile()}")
//            println("file createFile ${file.createfile}")
        }

        btn29_ad.click {
            val file =
                "${Environment.getExternalStorageDirectory().absolutePath}/aA_test/text.txt".toFile()

            println("file deleteFile ${file.deleteFile()}")
        }

        btn30_ad.click {
            val file =
                "${Environment.getExternalStorageDirectory().absolutePath}/aA_test/text.txt".toFile()

            println("file rename ${file.rename("text2.txt")}")
        }

        btn31_ad.click {
            val dir = "${Environment.getExternalStorageDirectory().absolutePath}/aA_test".toFile()
            val file =
                "${Environment.getExternalStorageDirectory().absolutePath}/aA_test/text.txt".toFile()
            println("dir absolutePath ${dir.absolutePath}")
            println("file absolutePath ${file.absolutePath}")

            println("dir isAvailableDir ${dir.isAvailableDir()}")
            println("dir isAvailableFile ${dir.isAvailableFile()}")

            println("file isAvailableDir ${file.isAvailableDir()}")
            println("file isAvailableFile ${file.isAvailableFile()}")

            println("file mimeType ${file.mimeType}")
            println("file extension ${file.extension}")

            dir.findListFiles(FileFilter {
                return@FileFilter it.isFile
            }).forEach {
                println("list file-> " + it.path)
            }
            println("list ---------------------- ")
            dir.findListFiles(FileFilter {
                return@FileFilter it.isDirectory
            }).forEach {
                println("list dir -> " + it.path)
            }

            val file3 =
                "${Environment.getExternalStorageDirectory().absolutePath}/aA_test/text_副本.txt".toFile()
            println("${file3.name} fileLength ${file3.getFileLength().byteToStr()}")

            println(
                "externalDir statFsTotalSize ${
                    Environment.getExternalStorageDirectory().getStatFsTotalSize().byteToStr()
                }"
            )
            println(
                "externalDir statFsAvailableSize ${
                    Environment.getExternalStorageDirectory().getStatFsAvailableSize().byteToStr()
                }"
            )
        }

        btn32_ad.click {
            val path = "${Environment.getExternalStorageDirectory().absolutePath}/aA_test/"
            val file = "${path}text.txt".toFile()
            val dir = "${path}test1".toFile()

            println("unzipAssetsFile ${mActivity.unzipAssetsFile("test1.zip", "${path}test1")}")

//            mActivity.openFile(file)

            println("file getLocalFileUri ${mActivity.getLocalFileUri(file)}")
            println("dir getLocalFileUri ${mActivity.getLocalFileUri(dir)}")

//            println("file writeString ${file.writeString("111")}")
//            println("file appendString ${file.writeString("\n123", true)}")

//            val file2 = "${path}test1/tt1.txt".toFile()
//            println("file writeInputStream ${file.writeInputStream(file2.inputStream())}")
//            println("file appendInputStream ${file.writeInputStream(file2.inputStream(), true)}")

//            println("file copyToPath ${file.copyToPath("${path}text_副本.txt")}")
//            println("file moveToPath ${file.moveToPath("${path}text_副本2.txt")}")

//            val dir2 = "${path}test1/t1/t3".toFile()
//            val dir3 = "${path}t3_副本".toFile()
//            println("dir copyToPath ${dir2.copyToPath(dir3)}")

        }

        btn33_ad.click {
            val intent = Intent()
            println("intentIsAvailable ${mActivity.intentIsAvailable(intent)}")

//            mActivity.openQQ()
            mActivity.openWeChat()

        }

        btn34_ad.click {
            val dir = "${Environment.getExternalStorageDirectory()}/aA_test".toFile()

            val file = "${Environment.getExternalStorageDirectory()}/aA_test/2b.jpg".toFile()
            val file2 = "${Environment.getExternalStorageDirectory()}/aA_test/e7.jpg".toFile()
            val file3 = "${Environment.getExternalStorageDirectory()}/aA_test/111.png".toFile()

//            mActivity.shareText("分享文字")
//            mActivity.shareTextImage("分享文字图片", file)
//            mActivity.shareTextImage("分享文字多图片", file, file2)

//            mActivity.dial("17745645645")
//            mActivity.sendSMS("1774564", "发送内容")
//            mActivity.browser("https://www.baidu.com")
//            mActivity.email("73279625@qq.com", content = "123")

            mActivity.quickShoot(file3) { code, data ->
                println("code $code, data $data")
            }
        }

        btn35_ad.click {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                println("isShowKeyboard-> ${soft_input.isShowKeyboard}")
                println("keyboardShowHeight-> ${soft_input.keyboardShowHeight}")

//                soft_input.toggleKeyboard()

            }

            mActivity.toggleSoftInput()
//            mActivity.showSoftInput()
//            mActivity.hideSoftInput()

//            soft_input.showSoftInput()
//            soft_input.hideSoftInput()
        }

        btn36_ad.click {
            println("app getMetaData ${mActivity.getAppMetaData("TEST_KEY")}")
            println("activity getMetaData ${mActivity.getMetaData("TEST_KEY2")}")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkStateManager.init(mActivity)
        }

        btn37_ad.click {
//            mActivity.openWirelessSettings()

            println("isNetworkConnected ${mActivity.isNetworkConnected()}")
            println("isWifiConnected ${mActivity.isWifiConnected()}")
            println("isMobileConnected ${mActivity.isMobileConnected()}")
            println("isEthernetConnected ${mActivity.isEthernetConnected()}")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NetworkStateManager.get().registerNetworkCallback(this@DemoActivity)
            }
        }

        btn38_ad.click {
            println("isNotificationsEnabled ${mActivity.isNotificationsEnabled()}")

            mActivity.sendNotification(100) {
                it.setContentTitle("100通知")
                it.setContentText("内容Text1111111111")
                it.setWhen(System.currentTimeMillis())
                it.setSmallIcon(R.mipmap.ic_launcher)
            }

            mActivity.sendNotification(200) {
                it.setContentTitle("200通知")
                it.setContentText("内容Text222222222")
                it.setWhen(System.currentTimeMillis())
                it.setSmallIcon(R.mipmap.ic_launcher)
            }

            launch {
                delay(500)
                println("setNotificationBarExpand true")
                mActivity.setNotificationBarExpand(true)
            }

            launch {
                delay(2000)
                println("cancelNotification")
                mActivity.cancelNotification(100)
            }

            launch {
                delay(4000)
                println("cancelAllNotification")
                mActivity.cancelAllNotification()
            }
        }

        btn39_ad.click {
            println("isDevicePhone ${mActivity.isDevicePhone()}")
            println("isSimCardReady ${mActivity.isSimCardReady()}")
            println("getSimOperatorName ${mActivity.getSimOperatorName()}")
            println("getSimOperatorByMnc ${mActivity.getSimOperatorByMnc()}")
            println("phoneBrand ${phoneBrand()}")
            println("phoneModel ${phoneModel()}")

            quickRequestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE)) {
                println("getDeviceId ${mActivity.getDeviceId()}")
                println("getSerial ${getSerial()}")
            }
        }

        btn40_ad.click {
            iv_qr_code.setQrCodeImageBitmap("生成二维码信息")
            println("code ${iv_qr_code.getQrCodeByBitmap()}")
        }

        btn41_ad.click {
            println("17745645645 isMobileNumber ${"17745645645".isMobileNumber()}")
            println("01114556677 isTelNumber ${"01114556677".isTelNumber()}")
            println("35001119900101237x isIdCard ${"35001119900101237x".isIdCard()}")
            println("456456@asd.com isEmail ${"456456@asd.com".isEmail()}")
            println("aaa://www.456.net isUrl ${"aaa://www.456.net".isUrl()}")
            println("http1://www.111.net isWebUrl ${"http1://www.111.net".isWebUrl()}")
            println("127.0.0.01 isIpAddress ${"127.0.0.01".isIpAddress()}")
            println("360000 isZhPostCode ${"360000".isZhPostCode()}")
        }

        btn42_ad.click {
            println("get status_bar_height ${mActivity.getSystemDimenIdentifier("status_bar_height")}")
            println("get ic_launcher_background ${mActivity.getAppDrawableIdentifier("ic_launcher_background")}")
            println("get anim_bottom_in ${mActivity.getAppAnimIdentifier("anim_bottom_in")}")
            println("get network_security_config ${mActivity.getAppXmlIdentifier("network_security_config")}")

            val file =
                "${Environment.getExternalStorageDirectory()}/aA_test/assetsFile.txt".toFile()
            val file2 = "${Environment.getExternalStorageDirectory()}/aA_test/rawFile.txt".toFile()

            println("assetsStr ${mActivity.assetsStr("test_file.txt")}")
            println("assetsCopyFile ${mActivity.assetsCopyFile("test_file.txt", file)}")

            println("rawStr ${mActivity.rawStr(R.raw.test_raw)}")
            println("rawCopyFile ${mActivity.rawCopyFile(R.raw.test_raw, file2)}")
        }

        btn43_ad.click {
            println("screenWidth ${mActivity.screenWidth}")
            println("screenHeight ${mActivity.screenHeight}")
            println("appScreenWidth ${mActivity.appScreenWidth}")
            println("appScreenHeight ${mActivity.appScreenHeight}")

            println("screenDensity $screenDensity")
            println("screenDensityDpi $screenDensityDpi")

            println("isFullScreen ${mActivity.isFullScreen}")
            println("isLandscape ${mActivity.isLandscape}")
            println("isPortrait ${mActivity.isPortrait}")
            println("screenRotation ${mActivity.screenRotation}")
            println("isScreenLock ${mActivity.isScreenLock()}")
            println("getScreenLockTime ${mActivity.getScreenLockTime()}")

            mActivity.screenShot(false) {
                iv_screen.setImageBitmap(it)
            }
        }

        btn44_ad.click {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                mActivity.quickRequestPermissions(arrayOf(Manifest.permission.FOREGROUND_SERVICE)) {
                    runService()
                }
            } else {
                runService()
            }
        }

        btn45_ad.click {
            println("string ${getString(R.string.test_string)}")
            println("string ${getString(R.string.test_string2, "123455")}")
            mActivity.resources.getStringArray(R.array.test_string_array).forEach {
                println("stringArray ->  $it")
            }

            println("toMD5 ${"我是md5源".toMd5()}")

            println("escapeSpecialWord ${"\\ \$ | ^".escapeSpecialWord()}")

            println("format ${(-75.011).format(prefix = "￥")}")
            println("format ${75.766.format(suffix = "元")}")

            // 2000-1-1 0:0:0
            val day1 = createGregorianCalendar(2000).timeInMillis
            // 2000-1-2 0:0:0
            val day2 = createGregorianCalendar(2000, 1, 2).timeInMillis
            // 2000-1-2 1:0:0
            val day3 = createGregorianCalendar(2000, 1, 2, 1).timeInMillis
            // 2000-1-2 1:5:0
            val day4 = createGregorianCalendar(2000, 1, 2, 1, 5).timeInMillis
            // 2000-1-2 1:5:30
            val day5 = createGregorianCalendar(2000, 1, 2, 1, 5, 30).timeInMillis
            // 2000-1-2 1:5:31
            val day6 = createGregorianCalendar(2000, 1, 2, 1, 5, 31).timeInMillis
            // 2010-1-1 0:0:0
            val day7 = createGregorianCalendar(2005).timeInMillis

            println("--> ${timeDifferenceMillis(day1, day2, TimeUnit.MILLISECONDS)}")
            println("--> ${timeDifferenceMillis(day1, day2, TimeUnit.DAYS)}")

            println("--> ${timeDifferenceString(day2, day3)}")
            println("--> ${timeDifferenceString(day3, day4)}")
            println("--> ${timeDifferenceString(day4, day5)}")
            println("--> ${timeDifferenceString(day5, day6)}")
            println("--> ${timeDifferenceString(day6, day7)}")

            println("isToday ${day7.isToday()}")
            println("getNowTimeString ${getNowTimeString()}")

            val now = Calendar.getInstance().timeInMillis

            val time0 = now - UnitObj.TIME_SECONDS_MILLIS
            val time1 = now - UnitObj.TIME_MINUTE_MILLIS
            val time2 = now - UnitObj.TIME_MINUTE_MILLIS * 4
            val time3 = now - UnitObj.TIME_HOUR_MILLIS
            val time4 = now - UnitObj.TIME_HOUR_MILLIS * 2
            val time5 = now - UnitObj.TIME_DAY_MILLIS
            val time6 = now - UnitObj.TIME_HOUR_MILLIS * 22
            val time7 = now - UnitObj.TIME_DAY_MILLIS * 5

            println("----> ${time0.nowTimeDifference()}")
            println("----> ${time1.nowTimeDifference()}")
            println("----> ${time2.nowTimeDifference()}")
            println("----> ${time3.nowTimeDifference()}")
            println("----> ${time4.nowTimeDifference()}")
            println("----> ${time5.nowTimeDifference()}")
            println("----> ${time6.nowTimeDifference()}")
            println("----> ${time7.nowTimeDifference()}")

            println("hidePhone ${"17745645645".hidePhone()}")
            println("hideName ${"张三".hideName()}")
            println("hideName ${"张三丰".hideName()}")
            println("hideSubstring ${"张三丰有限公司".hideSubstring(5, 5)}")
            println("getUrlLastName ${"http://www.baidu.com/xxx.ext".getUrlLastName()}")

            val sp = "StringExt".createImageSpannableStringBuilder(
                drawable = ActivityCompat.getDrawable(
                    mActivity,
                    R.mipmap.ic_launcher
                )
            )
            string_text.text = sp.matcherTextToColor(Color.RED, arrayOf("ext"), true)
        }

        btn46_ad.click {
            mActivity.quickRequestPermissions(arrayOf(Manifest.permission.VIBRATE)) {
                mActivity.startVibrate(10_000)
                launch {
                    delay(5000)
                    mActivity.cancelVibrate()
                }
            }
        }

        btn47_ad.click {
            println("viewWidth ${btn47_ad.viewWidth}")
            println("viewHeight ${btn47_ad.viewHeight}")
            println("isVisible ${btn47_ad.isVisible}")
            println("isLayoutRtl ${mActivity.isLayoutRtl()}")
        }

        btn48_ad.click {

            this@DemoActivity.setDeclaredField(this@DemoActivity.javaClass, "f1", "777")
            this@DemoActivity.setDeclaredField(this@DemoActivity.javaClass, "f2", "999")

            val tmp = this@DemoActivity.getDeclaredField(this@DemoActivity.javaClass, "f1")
            println("=======> getDeclaredField f1 -> $tmp")

            val tmp2 = this@DemoActivity.getDeclaredField(this@DemoActivity.javaClass, "f2")
            println("=======> getDeclaredField f2 -> $tmp2")

            println("=======> invokeDeclaredMethod m1")
            this@DemoActivity.invokeDeclaredMethod("m1")

            println("=======> invokeDeclaredMethod m2")
            this@DemoActivity.invokeDeclaredMethod(
                "m2",
                parameterTypes = arrayOf(String::class.java),
                args = arrayOf("1111")
            )
        }
    }

    private var f1 = "123"
    var f2 = "456"

    private fun m1() {
        println("我是 private fun m1")
    }

    fun m2(str: String) {
        println("我是 m2  参数 -> $str")
    }

    private fun runService() {
        launch {
            mActivity.startService<TestService>()
            println("isServiceRunning1 ${mActivity.isServiceRunning(TestService::class.java)}")

            mActivity.getAllRunningServiceNames().forEach {
                println("running $it")
            }

            delay(6000)
            mActivity.stopService<TestService>()
            println("isServiceRunning2 ${mActivity.isServiceRunning(TestService::class.java)}")
        }
    }

    override fun initData() {

    }

    override fun onConnected(type: Int) {
        println("连接转换 $type")
    }

    override fun onDisConnected() {
        println("连接断开")
    }

    override fun onDestroy() {
        super.onDestroy()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkStateManager.get().unregisterNetworkCallback(this@DemoActivity)
            NetworkStateManager.destroy()
        }

    }

}