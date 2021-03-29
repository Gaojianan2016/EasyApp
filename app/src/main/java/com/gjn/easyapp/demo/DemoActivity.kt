package com.gjn.easyapp.demo

import android.content.Intent
import com.gjn.easyapp.R
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easyutils.*
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : ABaseActivity() {

    override fun layoutId() = R.layout.activity_demo

    override fun initView() {

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

    }

    override fun initData() {

    }

}