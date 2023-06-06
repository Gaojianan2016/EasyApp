package com.gjn.easyapp.easynetworker

import com.gjn.easyapp.easyutils.*
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
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

object RetrofitManager {

    var printBody = true

    var isDebug = true

    var customInterceptorListener: OnCustomInterceptorListener? = null

    val ignoreLogUrlPath = mutableListOf<String>()

    val hideLogUrlPath = mutableListOf<String>()

    private val okHttpClientBuilder = OkHttpClient.Builder()
        .connectTimeout(30L, TimeUnit.SECONDS)
        .readTimeout(30L, TimeUnit.SECONDS)
        .writeTimeout(30L, TimeUnit.SECONDS)
        .sslSocketFactory(HttpsUtils.createSSLSocketFactory()!!, HttpsUtils.mX509TrustManager)
        .hostnameVerifier(HttpsUtils.mHostnameVerifier)
        .addInterceptor(LoggingAndCustomRequestInterceptor())

    /**
     * 快捷扩展创建方法
     * */
    fun <T> create(
        clazz: Class<T>,
        url: String,
        clientBuilder: OkHttpClient.Builder = okHttpClientBuilder,
        //添加自动过滤gson解析失败异常 转换器工厂
        converterFactoryArray: Array<Converter.Factory> = arrayOf(
            GsonConverterFactory.create(GsonBuilder().registerTypeAdapterFactory(GsonTypeAdapterFactory()).create())
        ),
        callAdapterFactoryArray: Array<CallAdapter.Factory> = arrayOf(),
        interceptorArray: Array<Interceptor> = arrayOf(),
        retrofitBuilderBlock: ((Retrofit.Builder) -> Unit)? = null
    ): T = createOriginal(clazz, url, clientBuilder, converterFactoryArray, callAdapterFactoryArray, interceptorArray, retrofitBuilderBlock)

    /**
     * 最原始创建方法
     * */
    fun <T> createOriginal(
        clazz: Class<T>,
        url: String,
        clientBuilder: OkHttpClient.Builder = okHttpClientBuilder,
        converterFactoryArray: Array<Converter.Factory> = arrayOf(),
        callAdapterFactoryArray: Array<CallAdapter.Factory> = arrayOf(),
        interceptorArray: Array<Interceptor> = arrayOf(),
        retrofitBuilderBlock: ((Retrofit.Builder) -> Unit)? = null
    ): T {
        //添加拦截器
        interceptorArray.forEach { clientBuilder.addInterceptor(it) }
        //创建build对象
        val builder = Retrofit.Builder().baseUrl(url).client(clientBuilder.build())
        //添加转换器
        converterFactoryArray.forEach { builder.addConverterFactory(it) }
        //添加编译器
        callAdapterFactoryArray.forEach { builder.addCallAdapterFactory(it) }
        //build其他操作
        retrofitBuilderBlock?.invoke(builder)
        //返回最终对象
        return builder.build().create(clazz)
    }

    private fun isPlaintext(buffer: Buffer): Boolean {
        kotlin.runCatching {
            val prefix = Buffer()
            val byteCount = buffer.size.coerceAtMost(64)
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) break
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        }.onFailure {
            it.printStackTrace()
        }
        return false
    }

    private fun log(msg: String) {
        if (isDebug) logD(msg, "EasyLogging")
    }

    fun requestBodyStr(request: Request): String {
        val bodyBuilder = StringBuilder()
        request.body?.let {
            if (it is FormBody) {
                for (i in 0 until it.size) {
                    bodyBuilder.append("${it.encodedName(i)} = ${it.encodedValue(i)}\n")
                }
            } else {
                val buffer = Buffer()
                it.writeTo(buffer)
                if (isPlaintext(buffer)) {
                    val charset = it.contentType()?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
                    bodyBuilder.append(buffer.readString(charset)).append("\n")
                }
            }
        }
        return bodyBuilder.toString()
    }

    fun responseBodyStr(response: Response, ignoreUrlArray: Array<String> = arrayOf()): String {
        val bodyBuilder = StringBuilder()
        ignoreUrlArray.forEach {
            if (response.request.url.toString().contains(it)) {
                return "ignore body log"
            }
        }
        if (response.body == null) return "body is null"
        kotlin.runCatching {
            response.body?.let {
                val source = it.source()
                source.request(Long.MAX_VALUE)
                val buffer = source.buffer
                if (isPlaintext(buffer)) {
                    val charset = it.contentType()?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
                    val result = buffer.clone().readString(charset)
                    if (result.isJsonStr()) {
                        bodyBuilder.append(result.formatJson())
                    } else {
                        bodyBuilder.append(result)
                    }
                }
            }
        }.onFailure {
            return "body parse error [${it.message}]"
        }
        return bodyBuilder.toString()
    }

    class LoggingAndCustomRequestInterceptor : Interceptor {

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

            if (hideLogUrlPath.contains(request.url.toString())) {
                log(buildString {
                    append("--> ${request.method} ${request.url}\n")
                    append("--> ${response.code} ${(t2 - t1) / 1e6}ms\n")
                })
            } else {
                log(buildString {
                    //request HEAD
                    append("----------Request HEAD----------\n")
                    append("--> ${request.method} ${request.url}\n")
                    request.headers.forEach { (name, value) ->
                        append("-> $name = $value\n")
                    }
                    //request BODY
                    append("----------Request BODY----------\n")
                    append("--> ${request.body?.contentType()} ${request.body?.contentLength()}\n")
                    append(requestBodyStr(request))
                    append("----------Request END----------\n")
                    //response HEAD
                    append("--> ${response.code} ${(t2 - t1) / 1e6}ms\n")
                    append("----------Response HEAD----------\n")
                    response.headers.forEach { (name, value) ->
                        append("-> $name = $value\n")
                    }
                    //response BODY
                    if (printBody) {
                        append("${request.url}\n")
                        append(responseBodyStr(response, ignoreLogUrlPath.toTypedArray()))
                    }
                })
            }
            return response
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
                        when (jr.peek()) {
                            JsonToken.STRING -> ""
                            JsonToken.NUMBER -> 0
                            JsonToken.BOOLEAN -> false
                            JsonToken.NULL -> null
                            else -> null
                        } as T?
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
                    else -> {}
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