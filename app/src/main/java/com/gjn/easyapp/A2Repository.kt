package com.gjn.easyapp

import com.gjn.easyapp.easynetworker.RetrofitManager
import okhttp3.Request
import okhttp3.Response

object A2Repository {

    suspend fun getGirls(): GirlBean{
        RetrofitManager.baseUrl = GankUrl.API_BASE
        RetrofitManager.customInterceptorListener =
            object : RetrofitManager.OnCustomInterceptorListener {
                override fun customRequest(url: String, builder: Request.Builder) {
                    builder.header("111", "222")
                }

                override fun getResponse(response: Response) {
                }
            }
        val api = RetrofitManager.create(GankUrl::class.java)

        return api.girls2(1, 10)
    }
}