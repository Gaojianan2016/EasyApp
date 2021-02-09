package com.gjn.easyapp.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.gjn.easyapp.A3Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

object MainRepository {

    suspend fun getGirls(page: Int = 1, count: Int = 10) = withContext(Dispatchers.IO) {
        val result = AppNetWorker.getInstant().gankApi.girls(page, count)
        //可以做 缓存数据 等操作
        //...
        result
    }

    suspend fun getGirls2(page: Int = 1, count: Int = 10) = withContext(Dispatchers.IO) {
        val result = AppNetWorker.getInstant().gankApi.girls(page, count).result()
        //可以做 缓存数据 等操作
        //...
        result
    }

    suspend fun getGirlsFlow(page: Int = 1, count: Int = 10) = flow {
        val result = AppNetWorker.getInstant().gankApi.girls(page, count)
        throw RuntimeException("手动创建的错误")
        //可以做 缓存数据 等操作
        //...
        emit(result)
    }.flowOn(Dispatchers.IO)

    fun getGirlsPagingData() = Pager(
        config = PagingConfig(
            // 每页数据
            pageSize = 10,
            // 开启占位符
            enablePlaceholders = true,
            // 预刷新的距离，距离最后一个 item 多远时加载数据
            prefetchDistance = 4,
            // 初始化加载数量，默认为 pageSize * 3
            initialLoadSize = 10
        )
    ) {
        A3Fragment.GirlPagaDataSource()
    }.flow

    suspend fun getBanner() = withContext(Dispatchers.IO) {
        val result = AppNetWorker.getInstant().wanApi.getBanner().result()
        //可以做 缓存数据 等操作
        //...
        result
    }

}

