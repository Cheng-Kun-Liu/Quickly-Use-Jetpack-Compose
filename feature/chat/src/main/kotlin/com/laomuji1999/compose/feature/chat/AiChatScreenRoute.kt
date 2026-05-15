package com.laomuji1999.compose.feature.chat

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object AiChatScreenRoute : NavKey {
    sealed interface Graph {
        data class Chat(val account: Long) : Graph
    }
}