package net.manyisoft.library.stroll.socketfactory

import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

/**
 * 网络证书（忽略证书）
 * Created on 2017/8/26.
 * @author liuxiongfei
 * @see
 */
object DefaultSocketFactory {

    fun getdefault(): SSLSocketFactory{

        val tm = arrayOf(CustomTrustManager())
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null,tm, SecureRandom())
        return sslContext.socketFactory
    }

    private class CustomTrustManager: X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

        override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()

    }
}