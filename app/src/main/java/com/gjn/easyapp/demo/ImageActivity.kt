package com.gjn.easyapp.demo

import android.content.Intent
import android.os.Build
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import com.gjn.easyapp.R
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easyutils.click
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity: ABaseActivity() {

    override fun layoutId() = R.layout.activity_image

    override fun onBundle() {
        val data = mBundle.getString(DATA, "空数据")
        val id = mBundle.getInt(ID, -1)
        println("data = $data ----- id = $id")

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

    }

    override fun initData() {
        img1_ai.click {
            setResult(777, Intent().apply { putExtra("data", "img1的数据") })
            finish()
        }
        img2_ai.click {
            setResult(456, Intent().apply { putExtra("data", "img2的数据") })
            finish()
        }
    }

    companion object{
        const val DATA = "DATA"
        const val ID = "ID"
    }
}