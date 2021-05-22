package com.gjn.easyapp.demo

import android.content.Intent
import com.gjn.easyapp.R
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easyutils.*
import kotlinx.android.synthetic.main.activity_demo.*
import kotlinx.android.synthetic.main.fragment_a1.*

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
            val newBmp = bmp?.scale(0.5f)
            println("newBmp new ${newBmp?.width} * ${newBmp?.height} = ${newBmp?.toByte()?.size} ")

            iv1_ad.setImageBitmap(newBmp)
        }

        btn7_ad.click {
            val logo = R.drawable.ic_launcher_foreground.vectorToBitmap(mActivity)
            val bmp2 = bmp.drawMiniBitmap(logo, 9, 0.5f)
            iv1_ad.setImageBitmap(bmp2)
        }

        btn8_ad.click {
            val bmp2 = bmp?.blurBitmap(mActivity)
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
            iv2_ad.setImageDrawable(this@DemoActivity.getAppIcon());
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

    }

    override fun initData() {

    }

}