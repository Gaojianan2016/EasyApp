package com.gjn.easyapp.model

data class WanResultData<T>(
    val errorCode: Int,
    val errorMsg: String,
    val `data`: T
) {
    fun result(): T {
        return when (errorCode) {
            CODE_SUCCESS -> data
            CODE_LOGIN_TIMEOUT -> throw WanException(errorCode, "登陆已过期")
            else -> throw WanException(errorCode, errorMsg)
        }
    }

    companion object {
        const val CODE_SUCCESS = 0
        const val CODE_LOGIN_TIMEOUT = -1001
    }
}