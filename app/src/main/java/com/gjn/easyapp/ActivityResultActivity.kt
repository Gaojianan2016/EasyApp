package com.gjn.easyapp

import android.content.Intent
import android.widget.Button
import com.gjn.easyapp.easybase.ABaseActivity
import com.gjn.easyapp.easyutils.click

class ActivityResultActivity : ABaseActivity() {

    override fun layoutId(): Int = R.layout.activity_result

    override fun initView() {

    }

    override fun initData() {
        findViewById<Button>(R.id.button).click {
            val intent = Intent()
            intent.putExtra("msg", "你接到了我的数据")
            setResult(100, intent)
            finish()
        }
    }
}