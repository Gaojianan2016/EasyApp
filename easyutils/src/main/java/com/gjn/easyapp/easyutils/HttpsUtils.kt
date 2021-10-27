package com.gjn.easyapp.easyutils

import android.annotation.SuppressLint
import android.util.Log
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

object HttpsUtils {

    private const val TAG = "HttpsUtils"

    val mHostnameVerifier = TrustAllHostnameVerifier()
    val mX509TrustManager = TrustAllManager()

    fun allowAllSSL() {
        HttpsURLConnection.setDefaultHostnameVerifier(mHostnameVerifier)
        HttpsURLConnection.setDefaultSSLSocketFactory(createSSLSocketFactory())
    }

    private fun createSSLSocketFactory(): SSLSocketFactory? {
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

    class TrustAllManager : X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            //接受任意客户端证书
        }

        @SuppressLint("TrustAllX509TrustManager")
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            //接受任意服务端证书
        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOfNulls(0)

    }

    class TrustAllHostnameVerifier : HostnameVerifier {
        //接受任意域名服务器
        @SuppressLint("BadHostnameVerifier")
        override fun verify(hostname: String?, session: SSLSession?): Boolean = true
    }
}