package com.gjn.easyapp.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

object MainRepository {

    suspend fun getGirls(page: Int = 1, count: Int = 10) = withContext(Dispatchers.IO){
        val result = AppNetWorker.getInstant().gankApi.girls(page, count)
        //可以做 缓存数据 等操作
        //...
        result
    }

    suspend fun getGirlsFlow(page: Int = 1, count: Int = 10) = flow {
        val result = AppNetWorker.getInstant().gankApi.girls(page, count)
//        throw RuntimeException("手动创建的错误")
        //可以做 缓存数据 等操作
        //...
        emit(result)
    }.flowOn(Dispatchers.IO)

    suspend fun getGirls2(page: Int = 1, count: Int = 10) = withContext(Dispatchers.IO){
        val result = AppNetWorker.getInstant().gankApi.girls(page, count).result()
        //可以做 缓存数据 等操作
        //...
        result
    }

    suspend fun getBanner() = withContext(Dispatchers.IO){
        val result = AppNetWorker.getInstant().wanApi.getBanner().result()
        //可以做 缓存数据 等操作
        //...
        result
    }

}

