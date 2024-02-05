package com.gjn.easyapp.easyutils

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.net.Socket
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

object HttpsUtils {

    private const val TAG = "HttpsUtils"

    val mHostnameVerifier = TrustAllHostnameVerifier()
    val mX509TrustManager = TrustAllManager()

    @RequiresApi(Build.VERSION_CODES.N)
    val mX509ExtendedTrustManager = ExtendedTrustAllManager()

    /**
     * 设置HttpsURLConnection默认允许全部Hostname和SSL
     * */
    fun allowAllSSL() {
        HttpsURLConnection.setDefaultHostnameVerifier(mHostnameVerifier)
        HttpsURLConnection.setDefaultSSLSocketFactory(createSSLSocketFactory())
    }

    fun createSSLSocketFactory(): SSLSocketFactory? {
        var factory: SSLSocketFactory? = null
        try {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf<TrustManager>(TrustAllManager()), SecureRandom())
            factory = sslContext.socketFactory
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "算法异常", e)
        } catch (e: KeyManagementException) {
            Log.e(TAG, "密钥管理异常", e)
        }
        return factory
    }

    @SuppressLint("TrustAllX509TrustManager")
    class TrustAllManager : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOfNulls(0)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("TrustAllX509TrustManager")
    class ExtendedTrustAllManager : X509ExtendedTrustManager() {
        override fun checkClientTrusted(
            chain: Array<out X509Certificate>?,
            authType: String?,
            socket: Socket?
        ) {
        }

        override fun checkClientTrusted(
            chain: Array<out X509Certificate>?,
            authType: String?,
            engine: SSLEngine?
        ) {
        }

        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun checkServerTrusted(
            chain: Array<out X509Certificate>?,
            authType: String?,
            socket: Socket?
        ) {
        }

        override fun checkServerTrusted(
            chain: Array<out X509Certificate>?,
            authType: String?,
            engine: SSLEngine?
        ) {
        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOfNulls(0)

    }

    class TrustAllHostnameVerifier : HostnameVerifier {
        //接受任意域名服务器
        @SuppressLint("BadHostnameVerifier")
        override fun verify(hostname: String?, session: SSLSession?): Boolean = true
    }
}