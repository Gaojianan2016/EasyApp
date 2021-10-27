package com.gjn.easyapp.network

import com.gjn.easyapp.GankUrl
import com.gjn.easyapp.WanUrl
import com.gjn.easyapp.easynetworker.RetrofitManager

class AppNetWorker {

    val gankApi = RetrofitManager.create(GankUrl::class.java, GankUrl.BASE_URL)

    val wanApi = RetrofitManager.create(WanUrl::class.java, WanUrl.BASE_URL)

    companion object {
        val netWorker by lazy { AppNetWorker() }
    }
}