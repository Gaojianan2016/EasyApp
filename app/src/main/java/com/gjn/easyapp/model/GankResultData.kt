package com.gjn.easyapp.model

data class GankResultData<T>(
    val `data`: T,
    val page: Int,
    val page_count: Int,
    val status: Int,
    val total_counts: Int
){
    fun result(): T{
        return when(status){
            CODE_SUCCESS -> data
            else -> throw GankException(status, "错误码 $status")
        }
    }

    companion object{
        const val CODE_SUCCESS = 100
    }
}