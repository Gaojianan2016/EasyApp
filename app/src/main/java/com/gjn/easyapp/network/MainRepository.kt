package com.gjn.easyapp.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object MainRepository {

    suspend fun getBannerFlow() = flow {
        val result = AppNetWorker.netWorker.wanApi.getBanner()
        //可以做 缓存数据 等操作
        //...
        emit(result)
    }.flowOn(Dispatchers.IO)

}

