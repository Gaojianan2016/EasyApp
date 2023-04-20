package com.gjn.easyapp.demo

import android.os.Build
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.widget.ImageView
import com.gjn.easyapp.R
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easyinitalizer.killAppByStack
import com.gjn.easyapp.easyutils.*

class ImageActivity : ABaseActivity() {

    override fun layoutId() = R.layout.activity_image

    override fun onBundle() {

        val data = getIntentKey(DATA, "空数据")
        val id = getIntentKey(ID, -1)
        val id2 = getIntentKey(ID2, -2)
        println("data = $data, id = $id, id2 = $id2")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            when (data) {
                DEFAULT_SCENE_TRANSITION_EXPLODE -> {
                    window.enterTransition = Explode()
                }
                DEFAULT_SCENE_TRANSITION_SLIDE -> {
                    window.enterTransition = Slide()
                }
                DEFAULT_SCENE_TRANSITION_FADE -> {
                    window.enterTransition = Fade()
                }
            }
            if (id != -1) {
                findViewById<ImageView>(R.id.img4_ai).setImageResource(id)
            }
        }

    }

    override fun initView() {
    }

    override fun initData() {
        findViewById<ImageView>(R.id.img1_ai).click {
//            finishActivityByStack(this@ImageActivity)
//            finishAllActivitiesByStack()
            mActivity.killAppByStack()
//            finishWithResult(777, "data" to "img1数据")
        }
        findViewById<ImageView>(R.id.img2_ai).click {
            finishWithResult("data" to "img2数据")
        }
    }

    companion object {
        const val DATA = "DATA"
        const val ID = "ID"
        const val ID2 = "ID2"
    }
}