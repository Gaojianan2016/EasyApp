package com.gjn.easyapp

class A2Repository {

    suspend fun getGirls(page: Int = 1, count: Int = 10) =
        AppNetWorker.getInstant().gankApi.girls(page, count).result()

    suspend fun getBanner() = AppNetWorker.getInstant().wanApi.getBanner().result()

}