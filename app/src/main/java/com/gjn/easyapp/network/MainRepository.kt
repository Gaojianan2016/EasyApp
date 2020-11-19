package com.gjn.easyapp.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MainRepository {

    suspend fun getGirls(page: Int = 1, count: Int = 10) = withContext(Dispatchers.IO){
        val result = AppNetWorker.getInstant().gankApi.girls(page, count).result()
        //可以做 缓存数据 等操作
        result
    }

    suspend fun getBanner() = withContext(Dispatchers.IO){
        val result = AppNetWorker.getInstant().wanApi.getBanner().result()
        //可以做 缓存数据 等操作
        result
    }

}

