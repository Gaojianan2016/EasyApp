package com.gjn.easyapp.easynetworker

import com.alibaba.fastjson.JSON
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type

class FastJsonResponseBodyConvert<T>(private val type: Type) : Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T? = value.use {
        JSON.parseObject(value.string(), type)
    }

}