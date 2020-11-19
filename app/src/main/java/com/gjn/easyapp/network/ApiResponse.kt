package com.gjn.easyapp.network

internal sealed class ApiResponse<T> {

    companion object {
        fun <T> create(body: T?): ApiResponse<T> =
            if (body == null) {
                ApiEmptyResponse()
            } else {
                ApiSuccessResponse(body)
            }

        fun <T> create(throwable: Throwable): ApiErrorResponse<T> =
            ApiErrorResponse(throwable)
    }
}

internal class ApiEmptyResponse<T> : ApiResponse<T>()

internal data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

internal data class ApiErrorResponse<T>(val throwable: Throwable) : ApiResponse<T>()