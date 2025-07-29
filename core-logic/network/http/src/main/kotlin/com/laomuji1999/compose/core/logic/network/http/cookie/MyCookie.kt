package com.laomuji1999.compose.core.logic.network.http.cookie

import io.ktor.http.Cookie
import io.ktor.http.CookieEncoding
import io.ktor.util.date.GMTDate
import kotlinx.serialization.Serializable

@Serializable
data class MyCookie(
    val name: String,
    val value: String,
    val encoding: CookieEncoding = CookieEncoding.URI_ENCODING,
    val maxAge: Int = 0,
    val timestamp: Long? = null,
    val domain: String? = null,
    val path: String? = null,
    val secure: Boolean = false,
    val httpOnly: Boolean = false,
    val extensions: Map<String, String?> = emptyMap()
){
    constructor(cookie: Cookie) : this(
        cookie.name,
        cookie.value,
        cookie.encoding,
        cookie.maxAge,
        cookie.expires?.timestamp,
        cookie.domain,
        cookie.path,
        cookie.secure,
        cookie.httpOnly,
        cookie.extensions
    )

    fun toCookie():Cookie{
        return Cookie(
            name = name,
            value = value,
            encoding = encoding,
            maxAge = maxAge,
            expires = if(timestamp != null) GMTDate(timestamp) else null,
            domain = domain,
            path = path,
            secure = secure,
            httpOnly = httpOnly,
            extensions = extensions
        )
    }
}