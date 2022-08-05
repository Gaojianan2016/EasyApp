package com.gjn.easyapp.easynetworker

import com.gjn.easyapp.easyutils.HttpsUtils
import com.gjn.easyapp.easyutils.formatJson
import com.gjn.easyapp.easyutils.logD
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import okhttp3.*
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.EOFException
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

object RetrofitManager {

    var printBody = true

    var isDebug = true

    var customInterceptorListener: OnCustomInterceptorListener? = null

    val ignoreLogUrlPath = mutableListOf<String>()

    var okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30L, TimeUnit.SECONDS)
        .readTimeout(30L, TimeUnit.SECONDS)
        .writeTimeout(30L, TimeUnit.SECONDS)
        .sslSocketFactory(HttpsUtils.createSSLSocketFactory()!!, HttpsUtils.mX509TrustManager)
        .hostnameVerifier(HttpsUtils.mHostnameVerifier)
        .addInterceptor(LoggingInterceptor())
        .build()

    fun <T> create(clazz: Class<T>, url: String): T =
        create(
            clazz, url,
            arrayOf(
                //添加自动过滤gson解析失败异常
                GsonConverterFactory.create(GsonBuilder().registerTypeAdapterFactory(GsonTypeAdapterFactory()).create())
            )
        )

    fun <T> create(
        clazz: Class<T>,
        url: String,
        factoryArray: Array<Converter.Factory> = arrayOf()
    ): T {
        val builder = Retrofit.Builder().baseUrl(url).client(okHttpClient)
        factoryArray.forEach { builder.addConverterFactory(it) }
        return builder.build().create(clazz)
    }

    private fun isPlaintext(buffer: Buffer): Boolean {
        return try {
            val prefix = Buffer()
            val byteCount = if (buffer.size < 64) buffer.size else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) break
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

    private fun log(msg: String) {
        if (isDebug) {
            logD(msg, "LoggingInterceptor")
        }
    }

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

            log("${printRequest(request)}${printResponse(response, t1, t2)}")
            if (printBody) {
                log("${request.url}\n${printResponseBody(response)}")
            }

            return response
        }

        private fun printRequest(request: Request): String {
            val log = StringBuilder()
            log.append("http request\n----------Request HEAD----------\n")
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
                    if (isPlaintext(buffer)) {
                        val charset = it.contentType()?.charset(StandardCharsets.UTF_8)
                            ?: StandardCharsets.UTF_8
                        log.append(buffer.readString(charset)).append("\n")
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
            ignoreLogUrlPath.forEach {
                if (response.request.url.toString() == it) {
                    return "ignore Log"
                }
            }
            if (response.body == null) {
                return "no Body"
            }
            response.body?.let {
                val source = it.source()
                source.request(Long.MAX_VALUE)
                val buffer = source.buffer
                if (isPlaintext(buffer)) {
                    val charset = it.contentType()?.charset(StandardCharsets.UTF_8)
                        ?: StandardCharsets.UTF_8
                    val result = buffer.clone().readString(charset)
                    if (result.startsWith("{\"") || result.startsWith("[{")) {
                        log.append(result.formatJson())
                    } else {
                        log.append(result)
                    }
                }
            }
            return log.toString()
        }
    }

    /**
     * 处理gson解析时类型不匹配或者空值问题。
     * https://www.jianshu.com/p/d8dcc656a06e
     */
    class GsonTypeAdapterFactory : TypeAdapterFactory {

        override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T> {
            val adapter = gson?.getDelegateAdapter(this, type)
            return object : TypeAdapter<T>() {
                @Throws(IOException::class)
                override fun write(out: JsonWriter?, value: T) {
                    adapter?.write(out, value)
                }

                @Throws(IOException::class)
                override fun read(jr: JsonReader): T? {
                    return try {
                        adapter?.read(jr)
                    } catch (e: Throwable) {
                        consumeAll(jr)
                        null
                    }
                }

            }
        }

        @Throws(IOException::class)
        private fun consumeAll(jr: JsonReader) {
            if (jr.hasNext()) {
                when (jr.peek()) {
                    JsonToken.STRING -> jr.nextString()
                    JsonToken.BEGIN_ARRAY -> {
                        jr.beginArray()
                        consumeAll(jr)
                        jr.endArray()
                    }
                    JsonToken.BEGIN_OBJECT -> {
                        jr.beginObject()
                        consumeAll(jr)
                        jr.endObject()
                    }
                    JsonToken.END_ARRAY -> jr.endArray()
                    JsonToken.END_OBJECT -> jr.endObject()
                    JsonToken.NUMBER -> jr.nextString()
                    JsonToken.BOOLEAN -> jr.nextBoolean()
                    JsonToken.NAME -> {
                        jr.nextName()
                        consumeAll(jr)
                    }
                    JsonToken.NULL -> jr.nextNull()
                    else -> {
                    }
                }
            }
        }
    }

    abstract class OnSimpleInterceptorListener : OnCustomInterceptorListener {
        override fun customRequest(url: String, builder: Request.Builder) {
        }

        override fun getResponse(response: Response) {
        }
    }

    interface OnCustomInterceptorListener {
        fun customRequest(url: String, builder: Request.Builder)

        fun getResponse(response: Response)
    }
}