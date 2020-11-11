package com.gjn.easyapp

import com.gjn.easyapp.easynetworker.RetrofitManager
import com.gjn.easyapp.model.WanBannerBean
import okhttp3.Request

class A2Repository {

    suspend fun getGirls(): GirlBean{
        RetrofitManager.baseUrl = GankUrl.API_BASE
        RetrofitManager.customInterceptorListener =
            object : RetrofitManager.OnSimpleInterceptorListener() {
                override fun customRequest(url: String, builder: Request.Builder) {
                    builder.header("111", "222")
                }
            }
        val api = RetrofitManager.create(GankUrl::class.java)

        return api.girls2(1, 10)
    }

    suspend fun getBanner(): List<WanBannerBean>{
        RetrofitManager.baseUrl = WanUrl.BASE_URL
        val api = RetrofitManager.create(WanUrl::class.java)
        return api.getBanner().apiData()
    }
}