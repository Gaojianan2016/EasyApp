package com.gjn.easyapp.demo

import com.gjn.easyapp.R
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easyutils.click
import com.gjn.easyapp.easyutils.startActivity
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity: ABaseActivity() {

    override fun layoutId() = R.layout.activity_demo

    override fun initView() {

        btn1_ad.click {
            ImageActivity::class.java.startActivity(mActivity, extrasMap = mapOf("name" to "数据1"))
        }
    }

    override fun initData() {

    }
}