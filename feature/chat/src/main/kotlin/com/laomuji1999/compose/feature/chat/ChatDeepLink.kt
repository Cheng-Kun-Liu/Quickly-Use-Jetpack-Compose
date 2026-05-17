package com.laomuji1999.compose.feature.chat

import android.net.Uri
import androidx.navigation3.runtime.NavKey
import com.laomuji1999.compose.feature.chat.chat.ChatScreenRoute

/**
 * 聊天模块的 DeepLink 处理
 */
object ChatDeepLink {
    const val SCHEME = "laomuji"
    const val HOST = "compose.laomuji1999.com"
    const val PATH_CHAT = "chat"

    /**
     * 解析 DeepLink 并返回需要添加到 backStack 的路由列表
     * 例如: laomuji://compose.laomuji1999.com/chat/123
     * 返回: [AiChatScreenRoute, ChatScreenRoute(123)]
     */
    fun parse(uri: Uri?): List<NavKey>? {
        if (uri == null) return null
        if (uri.scheme != SCHEME || uri.host != HOST) return null

        val segments = uri.pathSegments
        if (segments.size >= 2 && segments[0] == PATH_CHAT) {
            val account = segments[1].toLongOrNull() ?: return null
            return listOf(
                AiChatScreenRoute,
                ChatScreenRoute(account)
            )
        }
        return null
    }
}
