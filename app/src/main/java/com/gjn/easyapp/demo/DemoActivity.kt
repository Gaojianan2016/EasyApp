package com.gjn.easyapp.demo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Environment
import com.gjn.easyapp.R
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easyutils.*
import kotlinx.android.synthetic.main.activity_demo.*
import kotlinx.android.synthetic.main.fragment_a1.*
import kotlinx.coroutines.delay
import java.io.File
import java.io.FileFilter

class DemoActivity : ABaseActivity(), NetworkStateManager.OnNetworkStateListener {

    override fun layoutId() = R.layout.activity_demo

    @SuppressLint("MissingPermission")
    override fun initView() {
        val bmp = R.mipmap.test_img.toBitmap(mActivity)

        btn1_ad.click {
//            ImageActivity::class.java.startActivity(mActivity, extrasMap = mapOf(ImageActivity.DATA to "数据1"),
//                enterAnim = R.anim.anim_bottom_in, exitAnim = R.anim.anim_bottom_out)
            ImageActivity::class.java.startActivity(
                mActivity, extrasMap = mapOf(ImageActivity.DATA to "Explode"),
                options = mActivity.createOptionsBundle()
            )
//            ImageActivity::class.java.startActivity(mActivity, extrasMap = mapOf(ImageActivity.DATA to "Slide"),
//                options = mActivity.createOptionsBundle())
//            ImageActivity::class.java.startActivity(mActivity, extrasMap = mapOf(ImageActivity.DATA to "Fade"),
//                options = mActivity.createOptionsBundle())
        }

        btn2_ad.click {
//            ImageActivity::class.java.startActivity(
//                mActivity, extrasMap = mapOf(ImageActivity.DATA to "数据2"),
//                sharedElements = arrayOf(btn1_ad)
//            )
            ImageActivity::class.java.startActivity(
                mActivity, extrasMap = mapOf(ImageActivity.DATA to "数据3"),
                sharedElements = arrayOf(btn1_ad, btn2_ad)
            )
        }

        btn3_ad.click {
            ImageActivity::class.java.startActivity(
                mActivity, extrasMap = mapOf(
                    ImageActivity.DATA to "数据4",
                    ImageActivity.ID to R.mipmap.ic_launcher
                ),
                sharedElements = arrayOf(btn3_ad)
            )
        }

        btn4_ad.click {
            showToast("是否打开软键盘 ${mActivity.isSoftInputVisible()}")
        }

        btn5_ad.click {
            mActivity.simpleActivityResult(Intent(mActivity, ImageActivity::class.java))
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
            val logo = R.drawable.ic_launcher_foreground.vectorToBitmap(mActivity)
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
            CancelUtils.clearAppAllData(mActivity)
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
            println("statusBarHeight ${mActivity.statusBarHeight()}")
            println("isStatusBarVisible ${mActivity.isStatusBarVisible()}")
            println("isStatusBarLightMode ${mActivity.isStatusBarLightMode()}")

            println("actionBarHeight ${mActivity.application.actionBarHeight()}")

            println("navigationBarHeight ${mActivity.navigationBarHeight()}")
            println("isNavBarVisible ${mActivity.isNavBarVisible()}")
            println("isNavBarLightMode ${mActivity.isNavBarLightMode()}")
        }

        btn17_ad.click {
//            //设置状态栏
//            mActivity.setStatusBarColor(Color.TRANSPARENT, true)
//            //沉浸式状态栏
//            mActivity.setStatusBarColor(Color.TRANSPARENT)
//            //添加状态栏高度margin
//            nsv_ad.addMarginTopEqualStatusBarHeight()
//            //设置Light模式
//            mActivity.setStatusBarLightMode(true)
            showNextActivity(DrawerActivity::class.java)
        }

        btn18_ad.click {
            println("isAutoBrightnessEnabled ${mActivity.isAutoBrightnessEnabled()}")
            println("getMaxBrightness ${mActivity.getMaxBrightness()}")
            println("getBrightness ${mActivity.getBrightness()}")
            println("getWindowBrightness ${window.getWindowBrightness()}")
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
            println("sdkVersionName ${sdkVersionName()}")
            println("sdkVersionCode ${sdkVersionCode()}")
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
                "${Environment.getExternalStorageDirectory().absolutePath}/aA_test/text.txt".file()

            println("file createOrExistsFile ${file.createOrExistsFile()}")
//            println("file createFile ${file.createFile()}")
        }

        btn29_ad.click {
            val file =
                "${Environment.getExternalStorageDirectory().absolutePath}/aA_test/text.txt".file()

            println("file deleteFile ${file.deleteFile()}")
        }

        btn30_ad.click {
            val file =
                "${Environment.getExternalStorageDirectory().absolutePath}/aA_test/text.txt".file()

            println("file rename ${file.rename("text2.txt")}")
        }

        btn31_ad.click {
            val dir = "${Environment.getExternalStorageDirectory().absolutePath}/aA_test".file()
            val file =
                "${Environment.getExternalStorageDirectory().absolutePath}/aA_test/text.txt".file()
            println("dir absolutePath ${dir.absolutePath}")
            println("file absolutePath ${file.absolutePath}")

            println("dir isExistsDir ${dir.isExistsDir()}")
            println("dir isExistsFile ${dir.isExistsFile()}")

            println("file isExistsDir ${file.isExistsDir()}")
            println("file isExistsFile ${file.isExistsFile()}")

            println("file MimeType ${file.getMimeTypeFromExtension()}")

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
                "${Environment.getExternalStorageDirectory().absolutePath}/aA_test/text_副本.txt".file()
            println("${file3.fileName()} fileLength ${file3.fileLength().byteToStr()}")

            println(
                "externalDir getStatFsTotalSize ${
                    Environment.getExternalStorageDirectory().getStatFsTotalSize().byteToStr()
                }"
            )
            println(
                "externalDir getStatFsAvailableSize ${
                    Environment.getExternalStorageDirectory().getStatFsAvailableSize().byteToStr()
                }"
            )
        }

        btn32_ad.click {
            val path = "${Environment.getExternalStorageDirectory().absolutePath}/aA_test/"
            val file = "${path}text.txt".file()
            val dir = "${path}test1".file()

            println("unzipAssetsFile ${mActivity.unzipAssetsFile("test1.zip", "${path}test1")}")

//            mActivity.openFile(file)

            println("file getLocalFileUri ${mActivity.getLocalFileUri(file)}")
            println("dir getLocalFileUri ${mActivity.getLocalFileUri(dir)}")

//            println("file writeString ${file.writeString("111")}")
//            println("file appendString ${file.writeString("\n123", true)}")

//            val file2 = "${path}test1/tt1.txt".file()
//            println("file writeInputStream ${file.writeInputStream(file2.inputStream())}")
//            println("file appendInputStream ${file.writeInputStream(file2.inputStream(), true)}")

//            println("file copyToPath ${file.copyToPath("${path}text_副本.txt")}")
//            println("file moveToPath ${file.moveToPath("${path}text_副本2.txt")}")

//            val dir2 = "${path}test1/t1/t3".file()
//            val dir3 = "${path}t3_副本".file()
//            println("dir copyToPath ${dir2.copyToPath(dir3)}")

        }

        btn33_ad.click {
            val intent = Intent()
            println("intentIsAvailable ${mActivity.intentIsAvailable(intent)}")

//            mActivity.openQQ()
            mActivity.openWeChat()

        }

        btn34_ad.click {
            val dir = "${Environment.getExternalStorageDirectory()}/aA_test".file()

            val file = "${Environment.getExternalStorageDirectory()}/aA_test/2b.jpg".file()
            val file2 = "${Environment.getExternalStorageDirectory()}/aA_test/e7.jpg".file()

//            mActivity.shareText("分享文字")
//            mActivity.shareTextImage("分享文字图片", file)
//            mActivity.shareTextImage("分享文字多图片", file, file2)

//            mActivity.dialPhone("17745645645")
//            mActivity.sendSms("1774564", "发送内容")
//            mActivity.openBrowser("https://www.baidu.com")

            mActivity.quickPhotography(File(dir, "111.png")) { code, data ->
                println("code $code, data $data")
            }
        }

        btn35_ad.click {
            mActivity.toggleSoftInput()
//            mActivity.showSoftInput()
//            mActivity.hideSoftInput()

//            soft_input.showSoftInput()
//            soft_input.hideSoftInput()
//            mActivity.registerSoftInputChangedListener {
//                println("变化高度 $it")
//            }
        }

        btn36_ad.click {
            println("app getMetaData ${mActivity.getAppMetaData("TEST_KEY")}")
            println("activity getMetaData ${mActivity.getMetaData("TEST_KEY2")}")
        }

        NetworkStateManager.init(mActivity)

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
            println("areNotificationsEnabled ${mActivity.areNotificationsEnabled()}")

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
            println("isPhone ${mActivity.isPhone()}")
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
            println("35001119900101237x isTelNumber ${"35001119900101237x".isIdCard()}")
            println("456456@asd.com isEmail ${"456456@asd.com".isEmail()}")
            println("aaa://www.456.net isUrl ${"aaa://www.456.net".isUrl()}")
            println("127.0.0.01 isIpAddress ${"127.0.0.01".isIpAddress()}")
            println("360000 isZhPostCode ${"360000".isZhPostCode()}")
        }

        btn42_ad.click {
            println("get status_bar_height ${mActivity.getSystemDimenIdentifier("status_bar_height")}")
            println("get ic_launcher_background ${mActivity.getAppDrawableIdentifier("ic_launcher_background")}")
            println("get anim_bottom_in ${mActivity.getAppAnimIdentifier("anim_bottom_in")}")
            println("get network_security_config ${mActivity.getAppXmlIdentifier("network_security_config")}")

            val file = "${Environment.getExternalStorageDirectory()}/aA_test/assetsFile.txt".file()
            val file2 = "${Environment.getExternalStorageDirectory()}/aA_test/rawFile.txt".file()

            println("assetsStr ${mActivity.assetsStr("test_file.txt")}")
            println("assetsCopyFile ${mActivity.assetsCopyFile("test_file.txt", file)}")

            println("rawStr ${mActivity.rawStr(R.raw.test_raw)}")
            println("rawCopyFile ${mActivity.rawCopyFile(R.raw.test_raw, file2)}")
        }

        btn43_ad.click {
            println("screenWidth ${mActivity.screenWidth()}")
            println("screenHeight ${mActivity.screenHeight()}")
            println("appScreenWidth ${mActivity.appScreenWidth()}")
            println("appScreenHeight ${mActivity.appScreenHeight()}")

            println("getScreenDensity ${getScreenDensity()}")
            println("getScreenDensityDpi ${getScreenDensityDpi()}")

            println("isFullScreen ${mActivity.isFullScreen()}")
            println("isLandscape ${mActivity.isLandscape()}")
            println("isPortrait ${mActivity.isPortrait()}")
            println("screenRotation ${mActivity.screenRotation()}")
            println("isScreenLock ${mActivity.isScreenLock()}")
            println("getScreenLockTime ${mActivity.getScreenLockTime()}")

            iv_screen.setImageBitmap(mActivity.screenShot(false))
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
    }

    private fun runService() {
        launch {
            TestService::class.java.startService(mActivity)
            println("isServiceRunning1 ${mActivity.isServiceRunning(TestService::class.java)}")

            mActivity.getAllRunningServiceNames().forEach {
                println("running $it")
            }

            delay(6000)
            TestService::class.java.stopService(mActivity)
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
        }

        NetworkStateManager.destroy()
    }

}