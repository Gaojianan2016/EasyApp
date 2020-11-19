package com.gjn.easyapp.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineScope

fun <Type> CoroutineScope.requestLiveData(
    dsl: RequestAction<Type>.() -> Unit
): LiveData<ResultData<Type>>{
    val action = RequestAction<Type>().apply(dsl)

    return liveData {
        //开始请求
        emit(ResultData.start())

        val apiResponse = try {
            val result = action.api?.invoke()
            ApiResponse.create(result)
        }catch (e: Throwable){
            ApiResponse.create<Type>(e)
        }

        val result = when(apiResponse){
            is ApiEmptyResponse -> null
            is ApiSuccessResponse -> {
                apiResponse.body.apply {
                    //提交成功数据
                    emit(ResultData.success(this))
                }
            }
            is ApiErrorResponse -> {
                //提交错误数据
                emit(ResultData.error(apiResponse.throwable))
                null
            }
        }

        //提交完成数据
        emit(ResultData.complete(result))
    }
}