package com.laomuji1999.compose.feature.chat.chat

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.laomuji1999.compose.core.ui.extension.navOptionsPushBack
import kotlinx.serialization.Serializable

const val CHAT_SCREEN_DEEP_LINK = "laomuji://compose.laomuji1999.com/chat"

@Serializable
data class ChatScreenRoute(
    val account: Long
){

    sealed interface Graph {
        data object Back : Graph
    }

    companion object{
        fun NavHostController.navigateToChatScreen(account: Long, navOptions: NavOptions? = navOptionsPushBack()){
            return navigate(ChatScreenRoute(account), navOptions)
        }

        fun NavGraphBuilder.composeChatScreen(
            navigateToGraph: (Graph) -> Unit,
        ){
            composable<ChatScreenRoute>(
                deepLinks = listOf(
                    navDeepLink<ChatScreenRoute>(basePath = CHAT_SCREEN_DEEP_LINK)
                )
            ){
                ChatScreen(
                    navigateToGraph = navigateToGraph
                )
            }
        }
    }
}

