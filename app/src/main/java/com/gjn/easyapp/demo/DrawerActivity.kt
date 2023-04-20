package com.gjn.easyapp.demo

import android.graphics.Color
import com.gjn.easyapp.R
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easyutils.setStatusBarColor4Drawer

class DrawerActivity : ABaseActivity() {
    override fun layoutId(): Int = R.layout.activity_drawer

    override fun initView() {
//        //侧边栏
//        mActivity.setStatusBarColor4Drawer(findViewById(R.id.drawer_ad), findViewById(R.id.fakeView), Color.RED)
//        findViewById<View>(R.id.start_ad).addMarginTopEqualStatusBarHeight()
        //侧边栏
        mActivity.setStatusBarColor4Drawer(findViewById(R.id.drawer_ad), findViewById(R.id.fakeView), Color.RED, true)
    }

    override fun initData() {

    }

}