package com.laomuji1999.compose.core.logic.network.http.cookie

import com.laomuji1999.compose.core.logic.common.cache.CacheUtil
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import io.ktor.http.hostIsIp
import io.ktor.http.isSecure
import io.ktor.util.toLowerCasePreservingASCIIRules
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject


class MyCookiesStorage @Inject constructor(
    cacheUtil: CacheUtil
) : CookiesStorage {
    private var cacheCookie by cacheUtil.cacheable("cacheCookie","")
    private val mutex = Mutex()
    private val container: ArrayList<Cookie> = ArrayList()
    private val oldestCookie: AtomicLong = AtomicLong(0L)

    init {
        loadContainer()
    }

    override suspend fun get(requestUrl: Url): List<Cookie> = mutex.withLock {
        return@withLock container.filter {
            it.matches(requestUrl)
        }
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie): Unit = mutex.withLock {
        with(cookie) {
            if (name.isBlank()) return@withLock
        }

        container.removeAll { it.name == cookie.name && it.matches(requestUrl) }
        container.add(cookie.fillDefaults(requestUrl))
        cookie.expires?.timestamp?.let { expires ->
            if (oldestCookie.get() > expires) {
                oldestCookie.set(expires)
            }
        }
        saveContainer()
    }

    override fun close() {}

    private fun saveContainer(){
        val myCookieList = MyCookieList().copyFromCookeList(container)
        cacheCookie = Json.encodeToString(myCookieList)
    }

    private fun loadContainer(){
        try {
            val myCookieList: MyCookieList = Json.decodeFromString(cacheCookie)
            val saveList:List<Cookie> = myCookieList.toCookieList()
            container.addAll(saveList)
        }catch (_:Exception){ }
    }

    /**
     * 退出登录的时候可以删除cookie
     */
    fun removeAllCookies(){
        container.removeAll { true }
        saveContainer()
        loadContainer()
    }


    private fun Cookie.matches(requestUrl: Url): Boolean {
        val domain = domain?.toLowerCasePreservingASCIIRules()?.trimStart('.')
            ?: error("Domain field should have the default value")

        val path = with(path) {
            val current = path ?: error("Path field should have the default value")
            if (current.endsWith('/')) current else "$path/"
        }

        val host = requestUrl.host.toLowerCasePreservingASCIIRules()
        val requestPath = let {
            val pathInRequest = requestUrl.encodedPath
            if (pathInRequest.endsWith('/')) pathInRequest else "$pathInRequest/"
        }

        if (host != domain && (hostIsIp(host) || !host.endsWith(".$domain"))) {
            return false
        }

        if (path != "/" &&
            requestPath != path &&
            !requestPath.startsWith(path)
        ) {
            return false
        }

        return !(secure && !requestUrl.protocol.isSecure())
    }

    private fun Cookie.fillDefaults(requestUrl: Url): Cookie {
        var result = this

        if (result.path?.startsWith("/") != true) {
            result = result.copy(path = requestUrl.encodedPath)
        }

        if (result.domain.isNullOrBlank()) {
            result = result.copy(domain = requestUrl.host)
        }

        return result
    }
}