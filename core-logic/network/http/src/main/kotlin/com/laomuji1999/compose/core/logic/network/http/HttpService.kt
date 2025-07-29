package com.laomuji1999.compose.core.logic.network.http

import android.annotation.SuppressLint
import com.laomuji1999.compose.core.logic.common.Log
import com.laomuji1999.compose.core.logic.network.http.cookie.MyCookiesStorage
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

@Singleton
class HttpService @Inject constructor(
    val myCookiesStorage: MyCookiesStorage,
    val connectivityObserver: ConnectivityObserver,
) {
    companion object {
        private const val TAG = "tag_http"
    }

    val client = HttpClient {
        enableLog()
        trustAllCertificates()
        enableJsonConvert()
        enableTimeOut()
        keepCookies()
        enableRetry()
    }

    /**
     * 打印响应日志
     */
    private fun HttpClientConfig<*>.enableLog() {
        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.DEFAULT
            logger = object : Logger {
                override fun log(message: String) {
                    Log.debug(TAG, message)
                }
            }
        }
    }

    /**
     * 信任所有证书
     * 防止证书过期后不可用
     */
    private fun HttpClientConfig<*>.trustAllCertificates() {
        engine {
            this as OkHttpConfig
            config {
                val trustAllCert =
                    @SuppressLint("CustomX509TrustManager") object : X509TrustManager {
                        @SuppressLint("TrustAllX509TrustManager")
                        override fun checkClientTrusted(
                            chain: Array<out X509Certificate>?,
                            authType: String?,
                        ) {
                        }

                        @SuppressLint("TrustAllX509TrustManager")
                        override fun checkServerTrusted(
                            chain: Array<out X509Certificate>?,
                            authType: String?,
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                    }
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, arrayOf(trustAllCert), SecureRandom())
                sslSocketFactory(sslContext.socketFactory, trustAllCert)
            }
        }
    }

    /**
     * 支持 Json 转换
     * 必须添加 @Serializable 注解
     */
    private fun HttpClientConfig<*>.enableJsonConvert() {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
                encodeDefaults = true
            })
        }
    }

    /**
     * 超时时间
     */
    private fun HttpClientConfig<*>.enableTimeOut() {
        install(HttpTimeout) {
            requestTimeoutMillis = 120 * 1000
            connectTimeoutMillis = requestTimeoutMillis
            socketTimeoutMillis = requestTimeoutMillis
        }
    }

    /**
     * 保留 cookies
     */
    private fun HttpClientConfig<*>.keepCookies() {
        install(HttpCookies) {
            storage = myCookiesStorage
        }
    }

    /**
     * 开启超时重试
     */
    private fun HttpClientConfig<*>.enableRetry() {
        install(HttpRequestRetry) {
            maxRetries = 3
            retryOnExceptionIf { _, _ ->
                connectivityObserver.isConnected
            }
            exponentialDelay()
        }
    }
}