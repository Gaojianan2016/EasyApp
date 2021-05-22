package com.gjn.easyapp.demo

import android.graphics.Color
import com.gjn.easyapp.R
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easyutils.setStatusBarColor4Drawer
import kotlinx.android.synthetic.main.activity_drawer.*

class DrawerActivity : ABaseActivity(){
    override fun layoutId(): Int = R.layout.activity_drawer

    override fun initView() {
//        //侧边栏
//        mActivity.setStatusBarColor4Drawer(drawer_ad, fakeView, Color.RED)
//        start_ad.addMarginTopEqualStatusBarHeight()
        //侧边栏
        mActivity.setStatusBarColor4Drawer(drawer_ad, fakeView, Color.RED, true)
    }

    override fun initData() {

    }

}