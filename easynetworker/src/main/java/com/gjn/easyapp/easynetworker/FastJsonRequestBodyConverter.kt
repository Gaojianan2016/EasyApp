package com.gjn.easyapp.easynetworker

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializeConfig
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Converter

class FastJsonRequestBodyConverter<T>(
    private val serializeConfig: SerializeConfig
) : Converter<T, RequestBody> {

    override fun convert(value: T): RequestBody? =
        JSON.toJSONBytes(value, serializeConfig).toRequestBody(MEDIA_TYPE)

    companion object {
        private val MEDIA_TYPE: MediaType? = "application/json; charset=UTF-8".toMediaTypeOrNull()
    }
}