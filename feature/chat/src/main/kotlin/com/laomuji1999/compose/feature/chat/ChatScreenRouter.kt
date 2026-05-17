package com.laomuji1999.compose.feature.chat

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import com.laomuji1999.compose.core.ui.extension.nav3PopBackStack
import com.laomuji1999.compose.feature.chat.chat.ChatScreen
import kotlinx.serialization.Serializable

@Serializable
data object AiChatScreenRoute : NavKey {
    sealed interface Graph {
        data class Chat(val account: Long) : Graph
    }
}

@Serializable
data class ChatScreenRoute(
    val account: Long
) : NavKey

sealed interface ChatGraph {
    data object Back : ChatGraph
}

fun NavKey.provideChatNavEntry(
    backStack: MutableList<NavKey>
): NavEntry<NavKey>? {
    return when (val key = this) {
        is AiChatScreenRoute -> NavEntry(key) {
            AiChatScreen(
                navigateToGraph = { graph ->
                    when (graph) {
                        is AiChatScreenRoute.Graph.Chat -> backStack.add(ChatScreenRoute(graph.account))
                    }
                }
            )
        }

        is ChatScreenRoute -> NavEntry(key) {
            ChatScreen(
                account = key.account,
                navigateToGraph = { graph ->
                    when (graph) {
                        ChatGraph.Back -> backStack.nav3PopBackStack()
                    }
                }
            )
        }

        else -> null
    }
}
