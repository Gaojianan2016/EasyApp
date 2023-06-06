package com.gjn.easyapp.easynetworker

import com.alibaba.fastjson.JSON
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Converter

class FastJsonRequestBodyConverter<T> : Converter<T, RequestBody> {

    override fun convert(value: T): RequestBody = JSON.toJSONBytes(value).toRequestBody(MEDIA_TYPE)

    companion object {
        private val MEDIA_TYPE: MediaType? = "application/json; charset=UTF-8".toMediaTypeOrNull()
    }
}