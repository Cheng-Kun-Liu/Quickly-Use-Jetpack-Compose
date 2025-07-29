package com.laomuji1999.compose.core.logic.network.http.cookie

import io.ktor.http.Cookie
import kotlinx.serialization.Serializable

@Serializable
class MyCookieList(
    private var myCookieList:List<MyCookie>?=null
) {
    fun copyFromCookeList(list:List<Cookie>): MyCookieList {
        val arrList = ArrayList<MyCookie>()
        for (cookie in list) {
            arrList.add(MyCookie(cookie))
        }
        myCookieList = arrList
        return this
    }

    fun toCookieList():List<Cookie>{
        val arrList = ArrayList<Cookie>()
        for (cookie in myCookieList!!) {
            arrList.add(cookie.toCookie())
        }
        return arrList
    }
}