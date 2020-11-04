package com.gjn.easyapp.easynetworker

import com.gjn.easyapp.easyutils.JsonUtil
import com.gjn.easyapp.easyutils.logD
import okhttp3.*
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.EOFException
import java.io.IOException
import java.nio.charset.StandardCharsets

object RetrofitManager {

    var printBody = true

    var baseUrl = ""

    var customInterceptorListener: OnCustomInterceptorListener? = null

    var okHttpClient = OkHttpClient.Builder()
        .addInterceptor(LoggingInterceptor())
        .build()

    fun <T> create(clazz: Class<T>): T = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(clazz)

    class LoggingInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val request = original.newBuilder().apply {
                customInterceptorListener?.customRequest(original.url.toString(), this)
            }.build()
            val t1 = System.nanoTime()

            val response = chain.proceed(request)
            customInterceptorListener?.getResponse(response)
            val t2 = System.nanoTime()

            "${printRequest(request)}${printResponse(response, t1, t2)}".logD(TAG)
            if (printBody) {
                "${request.url}\n${printResponseBody(response)}".logD(TAG)
            }

            return response
        }

        private fun printRequest(request: Request): String {
            val log = StringBuilder()
            log.append("\n----------Request HEAD----------\n")
                .append("--> ${request.method} ${request.url}\n")
            request.headers.forEach { (name, value) ->
                log.append("-> $name = $value\n")
            }
            request.body?.let {
                log.append("----------Request BODY----------\n")
                    .append("--> ${it.contentType()} ${it.contentLength()}\n")
                if (it is FormBody) {
                    for (i in 0 until it.size) {
                        log.append("-> ${it.encodedName(i)} = ${it.encodedValue(i)}\n")
                    }
                } else {
                    val buffer = Buffer()
                    it.writeTo(buffer)
                    val charset = it.contentType()?.charset(StandardCharsets.UTF_8)
                        ?: StandardCharsets.UTF_8
                    if (isPlaintext(buffer)) {
                        log.append(buffer.readString(charset))
                    }
                }
            }
            log.append("----------Request END----------\n")
            return log.toString()
        }

        private fun printResponse(response: Response, startTime: Long, endTime: Long): String {
            val log = StringBuilder()
            log.append("--> ${response.code} ${(endTime - startTime) / 1e6}ms\n")
            log.append("----------Response HEAD----------\n")
            response.headers.forEach { (name, value) ->
                log.append("-> $name = $value\n")
            }
            return log.toString()
        }

        private fun printResponseBody(response: Response): String {
            val log = StringBuilder()
            if (response.body == null) {
                return "no Body"
            }
            response.body?.let {
                val source = it.source()
                source.request(Long.MAX_VALUE)
                val buffer = source.buffer
                val charset = it.contentType()?.charset(StandardCharsets.UTF_8)
                    ?: StandardCharsets.UTF_8
                val result = buffer.clone().readString(charset)
                if (result.startsWith("{\"") || result.startsWith("[{")) {
                    log.append(JsonUtil.formatJson(result))
                } else {
                    log.append(result)
                }
            }
            return log.toString()
        }

        companion object {
            const val TAG = "LoggingInterceptor"
        }
    }

    interface OnCustomInterceptorListener {
        fun customRequest(url: String, builder: Request.Builder)

        fun getResponse(response: Response)
    }

    fun isPlaintext(buffer: Buffer): Boolean {
        return try {
            val prefix = Buffer()
            val byteCount = if (buffer.size < 64) buffer.size else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            true
        } catch (e: EOFException) {
            false
        }
    }

}