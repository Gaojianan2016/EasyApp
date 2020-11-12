package com.gjn.easyapp

import com.gjn.easyapp.model.GankResultData
import retrofit2.http.GET
import retrofit2.http.Path

interface GankUrl {

    //https://gank.io/api/v2/data/category/Girl/type/Girl/page/1/count/10
    @GET("category/Girl/type/Girl/page/{page}/count/{count}")
    suspend fun girls(@Path("page") page: Int, @Path("count") count: Int): GankResultData<List<GirlBean>>

    companion object {
        const val BASE_URL = "https://gank.io/api/v2/data/"
    }
}