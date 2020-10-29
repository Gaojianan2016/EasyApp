package com.gjn.easyapp

import com.gjn.easyapp.easybase.ABaseActivity

class GalleryActivity : ABaseActivity() {

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

}