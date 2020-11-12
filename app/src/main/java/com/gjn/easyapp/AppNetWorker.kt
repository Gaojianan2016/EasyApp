package com.gjn.easyapp

import com.gjn.easyapp.easynetworker.RetrofitManager

class AppNetWorker {

    val gankApi = RetrofitManager.create(GankUrl::class.java, GankUrl.BASE_URL)

    val wanApi = RetrofitManager.create(WanUrl::class.java, WanUrl.BASE_URL)

    companion object {

        private var netWorker: AppNetWorker? = null

        fun getInstant(): AppNetWorker {
            if (netWorker == null) {
                synchronized(AppNetWorker::class.java) {
                    if (netWorker == null) {
                        netWorker = AppNetWorker()
                    }
                }
            }
            return netWorker!!
        }
    }
}