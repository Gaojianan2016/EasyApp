package com.gjn.easyapp.network

data class ResultData<T>(
    val requestStatus: RequestStatus,
    val data: T?,
    val error: Throwable? = null
) {

    companion object {
        fun <T> start(): ResultData<T> =
            ResultData(RequestStatus.START, null, null)

        fun <T> success(data: T?): ResultData<T> =
            ResultData(RequestStatus.SUCCESS, data, null)

        fun <T> error(error: Throwable?): ResultData<T> =
            ResultData(RequestStatus.ERROR, null, error)

        fun <T> complete(data: T?): ResultData<T> =
            ResultData(RequestStatus.COMPLETE, data, null)
    }
}