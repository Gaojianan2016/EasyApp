package com.gjn.easyapp

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GankUrl {

    //https://gank.io/api/v2/data/category/Girl/type/Girl/page/1/count/10
    @GET("category/Girl/type/Girl/page/{page}/count/{count}")
    fun girls(@Path("page") page: Int, @Path("count") count: Int): Call<ResponseBody>

    //https://gank.io/api/v2/data/category/Girl/type/Girl/page/1/count/10
    @GET("category/Girl/type/Girl/page/{page}/count/{count}")
    suspend fun girls2(@Path("page") page: Int, @Path("count") count: Int): GirlBean

    companion object {
        const val API_BASE = "https://gank.io/api/v2/data/"
    }
}