package com.gjn.easyapp.demo

import android.content.Intent
import android.graphics.Color
import android.os.Environment
import com.gjn.easyapp.R
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easyutils.*
import kotlinx.android.synthetic.main.activity_demo.*
import kotlinx.android.synthetic.main.fragment_a1.*
import kotlinx.coroutines.delay
import java.io.FileFilter

class DemoActivity : ABaseActivity() {

    override fun layoutId() = R.layout.activity_demo


    override fun initView() {
        val bmp = R.mipmap.test_img.toBitmap(mActivity)

        btn1_ad.click {
//            ImageActivity::class.java.startActivity(mActivity, extrasMap = mapOf(ImageActivity.DATA to "数据1"),
//                enterAnim = R.anim.anim_bottom_in, exitAnim = R.anim.anim_bottom_out)
            ImageActivity::class.java.startActivity(
                mActivity, extrasMap = mapOf(ImageActivity.DATA to "Explode"),
                options = createOptionsBundle(mActivity)
            )
//            ImageActivity::class.java.startActivity(mActivity, extrasMap = mapOf(ImageActivity.DATA to "Slide"),
//                options = createOptionsBundle(mActivity))
//            ImageActivity::class.java.startActivity(mActivity, extrasMap = mapOf(ImageActivity.DATA to "Fade"),
//                options = createOptionsBundle(mActivity))
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
            showToast("是否打开软键盘 ${mActivity.isKeyboardShowing()}")
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
            this@DemoActivity.requestWRPermission {
                this@DemoActivity.installApp("/sdcard/Android/jsq.apk")
            }
        }

        btn12_ad.click {
            this@DemoActivity.uninstallApp("com.ddnapalon.calculator.gp")
        }

        btn13_ad.click {
            this@DemoActivity.openApp("com.ddnapalon.calculator.gp")
        }

        btn14_ad.click {
            this@DemoActivity.openAppDetailsSettings("com.ddnapalon.calculator.gp")
        }

        btn15_ad.click {
            iv2_ad.setImageDrawable(this@DemoActivity.getAppIcon())
            println("iconId ${this@DemoActivity.getAppIconId()}")
            println("applicationName ${this@DemoActivity.getApplicationName()}")
            println("appPath ${this@DemoActivity.getAppPath()}")
            println("versionName ${this@DemoActivity.getAppVersionName()}")
            println("versionCode ${this@DemoActivity.getAppVersionCode()}")
            println("signaturesSHA1 ${this@DemoActivity.getAppSignaturesSHA1()}")
            println("signaturesSHA256 ${this@DemoActivity.getAppSignaturesSHA256()}")
            println("signaturesMD5 ${this@DemoActivity.getAppSignaturesMD5()}")
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
            mActivity.copyClipboardText(text = "我是复制文字")
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
    }

    override fun initData() {

    }

}