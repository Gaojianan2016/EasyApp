package com.gjn.easyapp.demo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.gjn.easyapp.R
import com.gjn.easyapp.easyutils.logE
import com.gjn.easyapp.easyutils.sendForegroundNotification

class TestService: Service() {

    override fun onCreate() {
        super.onCreate()
        logE("onCreate")

        sendForegroundNotification(111){
            it.setContentTitle("TestService通知")
            it.setContentText("TestService onCreate")
            it.setWhen(System.currentTimeMillis())
            it.setSmallIcon(R.mipmap.ic_launcher)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        logE("onStartCommand $intent $flags $startId")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        logE("onDestroy")
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}