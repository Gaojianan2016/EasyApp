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
import com.gjn.easyapp.databinding.ActivityDemoBinding
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easyutils.*
import kotlinx.coroutines.delay
import java.io.FileFilter
import java.util.*
import java.util.concurrent.TimeUnit

class DemoActivity : ABaseActivity(), NetworkStateManager.OnNetworkStateListener {

    private lateinit var binding: ActivityDemoBinding

    override fun layoutId() = R.layout.activity_demo

    override fun bindContentView() {
        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("MissingPermission")
    override fun initView() {
        val bmp = mActivity.toBitmap(R.mipmap.test_img)

        println("TimeZone ${TimeZone.getDefault()}")
        println("TimeZone displayName ${TimeZone.getDefault().displayName}")

        /*launchMain {
            RetrofitManager.ignoreLogUrlPath.add("banner/json")
            MainRepository.getBannerFlow().collectLatest {
                println("getBannerFlow success")
            }
        }*/

        binding.btn1Ad.click {
            //id动画
//            mActivity.startActivity<ImageActivity>(enterResId = R.anim.anim_bottom_in, exitResId = R.anim.anim_bottom_out)

            //特殊动画
            mActivity.startActivity<ImageActivity>(options = makeSceneTransitionAnimationBundle()) {
//                putExtra(ImageActivity.DATA, DEFAULT_SCENE_TRANSITION_EXPLODE)
//                putExtra(ImageActivity.DATA, DEFAULT_SCENE_TRANSITION_SLIDE)
                putExtra(ImageActivity.DATA, DEFAULT_SCENE_TRANSITION_FADE)
            }
        }

        binding.btn2Ad.click {
            mActivity.startActivity<ImageActivity>(sharedElements = arrayOf(binding.btn1Ad, binding.btn2Ad))
        }

        binding.btn3Ad.click {
            mActivity.startActivity<ImageActivity>(
                sharedElements = arrayOf(binding.btn3Ad),
                pairs = arrayOf(
                    ImageActivity.DATA to "数据4",
                    ImageActivity.ID to R.mipmap.ic_launcher
                )
            )
        }

        binding.btn4Ad.click {
            showToast("是否打开软键盘 ${mActivity.isSoftInputVisible()}")
        }

        binding.btn5Ad.click {
            mActivity.quickActivityResult(Intent(mActivity, ImageActivity::class.java))
            { code, data ->
                showToast("code=$code data=${data?.getStringExtra("data")}")
            }
        }

        binding.btn6Ad.click {
            println("bmp old ${bmp?.width} * ${bmp?.height} = ${bmp?.toByte()?.size} ")
            val bmp2 = bmp?.scale(0.5f)
            println("newBmp new ${bmp2?.width} * ${bmp2?.height} = ${bmp2?.toByte()?.size} ")
            binding.iv1Ad.setImageBitmap(bmp2)

//            val bmp3 = binding.btn7Ad.toBitmap()
//            iv1Ad.setImageBitmap(bmp3)

//            val bmp4 = logo?.scale(0.5f)
//            iv1Ad.setImageBitmap(bmp4)

//            val bmp5 = bmp?.clip(0, 0, 50, 50)
//            iv1Ad.setImageBitmap(bmp5)

//            val bmp6 = bmp?.skew(5f, 0f)
//            iv1Ad.setImageBitmap(bmp6)

//            val bmp7 = bmp?.rotate(50f)
//            iv1Ad.setImageBitmap(bmp7)

//            val bmp8 = bmp?.alpha()
//            iv1Ad.setImageBitmap(bmp8)

//            val bmp9 = bmp?.gray()
//            iv1Ad.setImageBitmap(bmp9)
        }

        binding.btn7Ad.click {

            val logo = mActivity.vectorToBitmap(R.drawable.ic_launcher_background)
            val bmp2 = bmp?.addImageWatermark(logo, 9, 0.5f, 88, -45f)
            binding.iv1Ad.setImageBitmap(bmp2)

//            val bmp3 = bmp?.toCircle(5, Color.BLUE)
//            iv1Ad.setImageBitmap(bmp3)

//            val bmp4 = bmp?.toRoundCorner(10f, 5, Color.RED)
//            iv1Ad.setImageBitmap(bmp4)

//            val bmp5 = bmp?.addTextWatermark("水印\n12345", textSize = 40f, degrees = -45f, alpha = 188)
//            iv1Ad.setImageBitmap(bmp5)
        }

        binding.btn8Ad.click {
            val bmp2 = bmp?.fastBlur(mActivity)
            binding.iv1Ad.setImageBitmap(bmp2)
        }

        binding.btn9Ad.click {
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

        binding.btn10Ad.click {
            mActivity.clearAppAllData()
            showToast("清理成功")
        }

        binding.btn11Ad.click {
            mActivity.requestWRPermission {
                mActivity.installApp("/sdcard/Android/jsq.apk")
            }
        }

        binding.btn12Ad.click {
            mActivity.uninstallApp("com.ddnapalon.calculator.gp")
        }

        binding.btn13Ad.click {
            mActivity.openApp("com.ddnapalon.calculator.gp")
        }

        binding.btn14Ad.click {
            mActivity.openAppDetailsSettings("com.ddnapalon.calculator.gp")
        }

        binding.btn15Ad.click {
            binding.iv2Ad.setImageDrawable(mActivity.getAppIcon())
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

        binding.btn16Ad.click {
            println("statusBarHeight ${mActivity.statusBarHeight}px")
            println("isStatusBarVisible ${mActivity.isStatusBarVisible}")
            println("isStatusBarLightMode ${mActivity.isStatusBarLightMode}")

            println("actionBarHeight ${mActivity.actionBarHeight}px")
            println("navigationBarHeight ${mActivity.navigationBarHeight}px")
            println("isNavBarVisible ${mActivity.isNavBarVisible}")
            println("isNavBarLightMode ${mActivity.isNavBarLightMode}")
        }

        binding.btn17Ad.click {
//            //设置状态栏颜色
//            mActivity.setStatusBarColor(Color.TRANSPARENT, true)

//            //沉浸式状态栏
//            mActivity.setStatusBarColor(Color.TRANSPARENT)
//            //添加状态栏高度margin
//            nsvAd.addMarginTopEqualStatusBarHeight()

//            //设置状态栏显示
//            mActivity.isStatusBarVisible = !mActivity.isStatusBarVisible
//            //设置状态栏模式
//            mActivity.isStatusBarLightMode = !mActivity.isStatusBarLightMode

            mActivity.startActivity<DrawerActivity>()
        }

        binding.btn18Ad.click {
            println("isAutoBrightnessEnabled ${mActivity.isAutoBrightnessEnabled()}")
            println("screenBrightness ${mActivity.screenBrightness}")
            println("screenMaxBrightness ${mActivity.screenMaxBrightness}")
            println("windowBrightness ${window.windowBrightness}")
        }

        binding.btn19Ad.click {
            window.setWindowBrightness(binding.sbAd.progress)
        }

        binding.btn20Ad.debouncingClick {
            mActivity.copyClipboardText("我是复制文字")
        }

        binding.btn21Ad.debouncingClick {
            println("getClipboardLabel ${mActivity.getClipboardLabel()}")
            println("getClipboardText ${mActivity.getClipboardText()}")
        }

        binding.btn22Ad.debouncingClick {
            mActivity.clearClipboard()
        }

        binding.btn23Ad.click {
            binding.tvColorAd.setBackgroundColor(randomColor())
        }

        binding.btn24Ad.click {
            val color = randomColor()
            binding.tvColorAd.setBackgroundColor(color)
            println("${color.parseArgbColor()} isLightColor? ${color.isLightColor()}")
        }

        binding.btn25Ad.click {
            var color = Color.BLACK
            binding.tvColorAd.setBackgroundColor(color)
            launch {
                delay(1000)
                color = color.changeColorAlpha(0.2f)
                binding.tvColorAd.setBackgroundColor(color)
                delay(1000)
                color = color.changeRedColorAlpha(0.3f)
                binding.tvColorAd.setBackgroundColor(color)
                delay(1000)
                color = color.changeBlueColorAlpha(0.4f)
                binding.tvColorAd.setBackgroundColor(color)
                delay(1000)
                color = color.changeGreenColorAlpha(0.5f)
                binding.tvColorAd.setBackgroundColor(color)
            }
        }

        binding.btn26Ad.click {
            println("isDeviceRooted ${isDeviceRooted()}")
            println("isAdbEnabled ${mActivity.isAdbEnabled()}")
            println("sdkVersionName $sdkVersionName")
            println("sdkVersionCode $sdkVersionCode")
            println("androidID ${mActivity.androidID()}")
            println("getMacAddress ${mActivity.getMacAddress()}")
        }

        binding.btn27Ad.click {
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

        binding.btn28Ad.click {
            val file = "${Environment.getExternalStorageDirectory().absolutePath}/aA_test/text.txt".toFile()

            println("file createOrExistsFile ${file.createOrExistsFile()}")
//            println("file createFile ${file.createfile}")
        }

        binding.btn29Ad.click {
            val file = "${Environment.getExternalStorageDirectory().absolutePath}/aA_test/text.txt".toFile()

            println("file deleteFile ${file.deleteFile()}")
        }

        binding.btn30Ad.click {
            val file = "${Environment.getExternalStorageDirectory().absolutePath}/aA_test/text.txt".toFile()

            println("file rename ${file.rename("text2.txt")}")
        }

        binding.btn31Ad.click {
            val dir = "${Environment.getExternalStorageDirectory().absolutePath}/aA_test".toFile()
            val file = "${Environment.getExternalStorageDirectory().absolutePath}/aA_test/text.txt".toFile()
            println("dir absolutePath ${dir.absolutePath}")
            println("file absolutePath ${file.absolutePath}")

            println("dir isAvailableDir ${dir.isAvailableDir()}")
            println("dir isAvailableFile ${dir.isAvailableFile()}")

            println("file isAvailableDir ${file.isAvailableDir()}")
            println("file isAvailableFile ${file.isAvailableFile()}")

            println("file actualName ${file.actualFileName}")
            println("file suffix ${file.suffix}")
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

            val file3 = "${Environment.getExternalStorageDirectory().absolutePath}/aA_test/text_副本.txt".toFile()
            println("${file3.name} fileLength ${file3.getFileLength().byteToStr()}")

            println("externalDir statFsTotalSize ${Environment.getExternalStorageDirectory().getStatFsTotalSize().byteToStr()}")
            println("externalDir statFsAvailableSize ${Environment.getExternalStorageDirectory().getStatFsAvailableSize().byteToStr()}")
        }

        binding.btn32Ad.click {
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

        binding.btn33Ad.click {
            val intent = Intent()
            println("intentIsAvailable ${mActivity.intentIsAvailable(intent)}")

//            mActivity.openQQ()
            mActivity.openWeChat()

        }

        binding.btn34Ad.click {
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

        binding.btn35Ad.click {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                println("isShowKeyboard-> ${binding.softInput.isShowKeyboard}")
                println("keyboardShowHeight-> ${binding.softInput.keyboardShowHeight}")

//                soft_input.toggleKeyboard()

            }

            mActivity.toggleSoftInput()
//            mActivity.showSoftInput()
//            mActivity.hideSoftInput()

//            soft_input.showSoftInput()
//            soft_input.hideSoftInput()
        }

        binding.btn36Ad.click {
            println("app getMetaData ${mActivity.getAppMetaData("TEST_KEY")}")
            println("activity getMetaData ${mActivity.getMetaData("TEST_KEY2")}")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkStateManager.init(mActivity)
        }

        binding.btn37Ad.click {
//            mActivity.openWirelessSettings()

            println("isNetworkConnected ${mActivity.isNetworkConnected()}")
            println("isWifiConnected ${mActivity.isWifiConnected()}")
            println("isMobileConnected ${mActivity.isMobileConnected()}")
            println("isEthernetConnected ${mActivity.isEthernetConnected()}")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NetworkStateManager.get().registerNetworkCallback(this@DemoActivity)
            }
        }

        binding.btn38Ad.click {
            kotlin.runCatching {
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
        }

        binding.btn39Ad.click {
            println("isDevicePhone ${mActivity.isDevicePhone()}")
            println("isSimCardReady ${mActivity.isSimCardReady()}")
            println("getSimOperatorName ${mActivity.getSimOperatorName()}")
            println("getSimOperatorByMnc ${mActivity.getSimOperatorByMnc()}")
            println("phoneBrand ${phoneBrand()}")
            println("phoneModel ${phoneModel()}")

            quickRequestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE)) {
                println("getDeviceId ${mActivity.getDeviceId()}")
                try {
                    val sn = getSerial()
                    println("getSerial $sn")
                } catch (e: Exception) {
                    e.printStackTrace()
                    println("getSerial 未知")
                }
            }
        }

        binding.btn40Ad.click {
            binding.ivQrCode.setQrCodeImageBitmap("生成二维码信息")
            println("code ${binding.ivQrCode.getQrCodeByBitmap()}")
        }

        binding.btn41Ad.click {
            println("17745645645 isMobileNumber ${"17745645645".isMobileNumber()}")
            println("01114556677 isTelNumber ${"01114556677".isTelNumber()}")
            println("35001119900101237x isIdCard ${"35001119900101237x".isIdCard()}")
            println("456456@asd.com isEmail ${"456456@asd.com".isEmail()}")
            println("aaa://www.456.net isUrl ${"aaa://www.456.net".isUrl()}")
            println("http1://www.111.net isWebUrl ${"http1://www.111.net".isWebUrl()}")
            println("127.0.0.01 isIpAddress ${"127.0.0.01".isIpAddress()}")
            println("360000 isZhPostCode ${"360000".isZhPostCode()}")
        }

        binding.btn42Ad.click {
            requestWRPermission {
                try {
                    println("get status_bar_height ${mActivity.getSystemDimenIdentifier("status_bar_height")}")
                    println("get ic_launcher_background ${mActivity.getAppDrawableIdentifier("ic_launcher_background")}")
                    println("get anim_bottom_in ${mActivity.getAppAnimIdentifier("anim_bottom_in")}")
                    println("get network_security_config ${mActivity.getAppXmlIdentifier("network_security_config")}")

                    val file = "${Environment.getExternalStorageDirectory()}/aA_test/assetsFile.txt".toFile()
                    val file2 = "${Environment.getExternalStorageDirectory()}/aA_test/rawFile.txt".toFile()

                    println("assetsStr ${mActivity.assetsStr("test_file.txt")}")
                    println("assetsCopyFile ${mActivity.assetsCopyFile("test_file.txt", file)}")

                    println("rawStr ${mActivity.rawStr(R.raw.test_raw)}")
                    println("rawCopyFile ${mActivity.rawCopyFile(R.raw.test_raw, file2)}")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        binding.btn43Ad.click {
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
                binding.ivScreen.setImageBitmap(it)
            }
        }

        binding.btn44Ad.click {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                mActivity.quickRequestPermissions(arrayOf(Manifest.permission.FOREGROUND_SERVICE)) {
                    runService()
                }
            } else {
                runService()
            }
        }

        binding.btn45Ad.click {
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
            println("getNowTimeString UTC ${getNowTimeString(timeZone = TimeZone.getTimeZone("UTC"))}")
            println("getNowTimeString GMT ${getNowTimeString(timeZone = TimeZone.getTimeZone("GMT"))}")
            println("getNowTimeZone1 ${currentTimeZone()}")
            println("getNowTimeZone2 ${currentTimeZone("%02d")}")
            println("getNowTimeZone3 ${currentTimeZone("%02d", TimeZone.getTimeZone("GMT-7:00"))}")

            val testDay = 1675418071000
            println("testDay0 ${testDay.toDateFormat()}")
            println("testDay1 ${testDay.toDateFormat("MM-dd-yyyy HH:mm")}")
            println("testDay2 ${testDay.toDateFormat("MM-dd-yyyy hh:mm", Locale.US)}")
            println("testDay3 ${testDay.toDateFormat("MM-dd-yyyy hh:mm", Locale.US, TimeZone.getDefault())}")
            println("testDay4 ${testDay.toDateFormat("MM-dd-yyyy hh:mm", Locale.US, TimeZone.getTimeZone("GMT-5:00"))}")
            println("testDay5 ${testDay.toDateFormat("MM-dd-yyyy hh:mm", Locale.getDefault(), TimeZone.getTimeZone("GMT-5:00"))}")

            println("timezone1 ${createTimeZone(-12)}")
            println("timezone2 ${createTimeZone(12)}")
            println("timezone3 ${createTimeZone(8)}")
            println("timezone4 ${createTimeZone(-5)}")

            val now = Calendar.getInstance().timeInMillis

            val time0 = now - 1.secondsMillis
            val time1 = now - 1.minuteMillis
            val time2 = now - 4.minuteMillis
            val time3 = now - 1.hourMillis
            val time4 = now - 2.hourMillis
            val time5 = now - 1.daysMillis
            val time6 = now - 22.hourMillis
            val time7 = now - 5.daysMillis

            println("time----> ${time0.nowTimeDifference()}")
            println("time----> ${time1.nowTimeDifference()}")
            println("time----> ${time2.nowTimeDifference()}")
            println("time----> ${time3.nowTimeDifference()}")
            println("time----> ${time4.nowTimeDifference()}")
            println("time----> ${time5.nowTimeDifference()}")
            println("time----> ${time6.nowTimeDifference()}")
            println("time----> ${time7.nowTimeDifference()}")

            println("hidePhone ${"17745645645".hidePhone()}")
            println("hideName ${"张三".hideName()}")
            println("hideName ${"张三丰".hideName()}")
            println("hideSubstring ${"张三丰有限公司".hideSubstring(5, 5)}")
            println("getUrlLastName ${"http://www.baidu.com/xxx.ext".getUrlLastName()}")
            println("getUrlLastActualName ${"http://www.baidu.com/xxx.ext".getUrlLastActualName()}")

            val size0 = 15.kbByte
            val size1 = 22L.mbByte_B
            val size2 = 77L.gbByte
            val size3 = 66.tbByte_B
            val size4 = 66L.tbByte
            val size5 = 554

            println("size $size0----> ${size0.byteToStr()}")
            println("size $size1----> ${size1.byteToStr(true)}")
            println("size $size2----> ${size2.byteToStr()}")
            println("size $size3----> ${size3.byteToStr(true)}")
            println("size $size4----> ${size4.byteToStr()}")
            println("size $size5----> ${size5.byteToStr()}")

            val sp = "StringExt".createImageSpannableStringBuilder(
                drawable = ActivityCompat.getDrawable(
                    mActivity,
                    R.mipmap.ic_launcher
                )
            )
            binding.stringText.text = sp.matcherTextToColor(Color.RED, arrayOf("ext"), true)
        }

        binding.btn46Ad.click {
            mActivity.quickRequestPermissions(arrayOf(Manifest.permission.VIBRATE)) {
                mActivity.startVibrate(10_000)
                launch {
                    delay(5000)
                    mActivity.cancelVibrate()
                }
            }
        }

        binding.btn47Ad.click {
            println("viewWidth ${binding.btn47Ad.viewWidth}")
            println("viewHeight ${binding.btn47Ad.viewHeight}")
            println("isVisible ${binding.btn47Ad.isVisible}")
            println("isLayoutRtl ${mActivity.isLayoutRtl()}")
        }

        binding.btn48Ad.click {

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

        binding.btn49Ad.click {
            val json1 = "{\"name\":\"111\",\"age\":11,\"phone\":\"123123213\",\"email\":\"123123123@123.com\"}"
            val json2 = "[{\"name\":\"111\",\"age\":11,\"phone\":\"123123213\",\"email\":\"123123123@123.com\"}," +
                    "{\"name\":\"222\",\"age\":22,\"phone\":\"456456456456\",\"email\":\"456456456456@123.com\"}]"
            val json3 = "{\"data\":{\"name\":\"111\",\"age\":\"11\",\"phone\":\"123123213\",\"email\":\"123123123@123.com\"}}"
            val json4 = "{\"data\":[{\"name\":\"111\",\"age\":\"11\",\"phone\":\"123123213\",\"email\":\"123123123@123.com\"}," +
                    "{\"name\":\"222\",\"age\":\"22\",\"phone\":\"456456456456\",\"email\":\"456456456456@123.com\"}]}"

            val json3b = json3.fromGsonJson<BaseTestJsonBean<TestJsonBean>>(BaseTestJsonBean::class.java, TestJsonBean::class.java)
            val json4b = json4.fromGsonJson<BaseTestJsonListBean<TestJsonBean>>(BaseTestJsonListBean::class.java, TestJsonBean::class.java)

            println("json1 fromGsonJson ${json1.fromGsonJson(TestJsonBean::class.java)}")
            println("json2 fromGsonJson ${json2.fromGsonJsonList<TestJsonBean>()}")
            println("json3b fromGsonJson $json3b")
            println("json4b fromGsonJson $json4b")

            println("json1 formatJson \n${json1.formatJson()}\n\n")

            val jsonBean1 = TestJsonBean("333", 33, "789789", "66666@33.com")
            val jsonBean2 = mutableListOf(
                TestJsonBean("444", 44, "44789789", "4466666@44.com"),
                TestJsonBean("555", 55, "55789789", "5566666@55.com"),
            )
            val jsonBean3 = BaseTestJsonBean(jsonBean1)
            val jsonBean4 = BaseTestJsonBean(jsonBean2)

            println("jsonBean1 toGsonJson ${jsonBean1.toGsonJson()}")
            println("jsonBean2 toGsonJson ${jsonBean2.toGsonJson()}")
            println("jsonBean3 toGsonJson ${jsonBean3.toGsonJson()}")
            println("jsonBean4 toGsonJson ${jsonBean4.toGsonJson()}")
        }

        binding.btn50Ad.click {
            val log = buildString {
                for (j in 0..170) {
                    append("[$j]{床前明月光，疑是地上霜。举头望明月，低头思故乡。}")
                }
            }
            val log2 = buildString {
                for (j in 0..30) {
                    append("[$j]{床前明月光，疑是地上霜。举头望明月，低头思故乡。}")
                }
            }
            println(log.length)
            println(log2.length)
            printlnSuper(log)
            printlnSuper(log2)
//            logV(log, tr = Throwable("logV 测试"))
//            logD(log, tr = Throwable("logD 测试"))
            logE(log, tr = Throwable("logE 测试"))
            logE(log2, tr = Throwable("logE 测试2"))
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

            var services = mActivity.getAllRunningServiceNames()
            println("services1 ${services.size}")
            services.forEach {
                println("running $it")
            }

            delay(6000)
            mActivity.stopService<TestService>()
            println("isServiceRunning2 ${mActivity.isServiceRunning(TestService::class.java)}")

            services = mActivity.getAllRunningServiceNames()
            println("services2 ${services.size}")
            services.forEach {
                println("running $it")
            }
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

    data class BaseTestJsonBean<T>(
        val data: T
    )

    data class BaseTestJsonListBean<T>(
        val data: List<T>
    )

    data class TestJsonBean(
        val name: String,
        val age: Int,
        val phone: String,
        val email: String,
        val list: List<TestJsonBean>? = null
    )

}