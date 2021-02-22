package com.gjn.easyapp

import android.os.Bundle
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easyutils.AutoScreenUtil
import com.gjn.easyapp.easyutils.IAutoChange

class GalleryActivity : ABaseActivity(), IAutoChange {

    override fun preCreate(savedInstanceState: Bundle?) {
        super.preCreate(savedInstanceState)
        AutoScreenUtil.setCustomDensity(this)
    }

    override fun layoutId(): Int = R.layout.activity_gallery

    override fun onBundle() {
        val msg = mBundle.getString("msg", "")
        println("接到msg $msg")
    }

    override fun initView() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fl_ag, GalleryFragment())
            .commit()
    }

    override fun initData() {

    }

    override fun newWidth(): Float = 410f

}