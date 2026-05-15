package com.laomuji1999.compose.feature.chat.chat

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

const val CHAT_SCREEN_DEEP_LINK = "laomuji://compose.laomuji1999.com/chat"

@Serializable
data class ChatScreenRoute(
    val account: Long
) : NavKey {
    sealed interface Graph {
        data object Back : Graph
    }
}
