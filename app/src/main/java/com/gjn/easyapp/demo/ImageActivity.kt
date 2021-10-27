package com.gjn.easyapp.demo

import android.os.Build
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import com.gjn.easyapp.R
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easyutils.activityList
import com.gjn.easyapp.easyutils.click
import com.gjn.easyapp.easyutils.finishWithResult
import com.gjn.easyapp.easyutils.topActivity
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity: ABaseActivity() {

    override fun layoutId() = R.layout.activity_image

    override fun onBundle() {
        val data = mBundle.getString(DATA, "空数据")
        val id = mBundle.getInt(ID, -1)
        val id2 = mBundle.getInt(ID2, -1)
        println("data = $data, id = $id, id2 = $id2")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            when (data) {
                "Explode" -> {
                    window.enterTransition = Explode()
                }
                "Slide" -> {
                    window.enterTransition = Slide()
                }
                "Fade" -> {
                    window.enterTransition = Fade()
                }
            }
            if (id != -1){
                img4_ai.setImageResource(id)
            }
        }

    }

    override fun initView() {

        activityList.forEach {
            println("activityList222 -> ${it.javaClass.simpleName}")
            println("    ---> isFinishing ${it.isFinishing}")
        }
        println("activity top -> ${topActivity.javaClass.simpleName}")
    }

    override fun initData() {
        img1_ai.click {
            finishWithResult(777, "data" to "img1数据")
        }
        img2_ai.click {
            finishWithResult("data" to "img2数据")

        }
    }

    companion object{
        const val DATA = "DATA"
        const val ID = "ID"
        const val ID2 = "ID2"
    }
}