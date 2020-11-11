package com.gjn.easyapp

import com.gjn.easyapp.model.WanBannerBean
import com.gjn.easyapp.model.WanResultData
import retrofit2.http.GET

interface WanUrl {

    @GET("banner/json")
    suspend fun getBanner(): WanResultData<List<WanBannerBean>>

    companion object{
        var BASE_URL = "https://www.wanandroid.com/"
    }
}