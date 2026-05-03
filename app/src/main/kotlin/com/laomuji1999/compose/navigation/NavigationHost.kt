package com.laomuji1999.compose.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.laomuji1999.compose.WebViewActivity
import com.laomuji1999.compose.core.ui.extension.safePopBackStack
import com.laomuji1999.compose.core.ui.screen.SlideNavigation
import com.laomuji1999.compose.feature.video.VideoPlayActivity
import com.laomuji1999.compose.feature.chat.AiChatScreenRoute
import com.laomuji1999.compose.feature.chat.AiChatScreenRoute.composeAiChatScreen
import com.laomuji1999.compose.feature.chat.AiChatScreenRoute.navigateToAiChatScreen
import com.laomuji1999.compose.feature.chat.chat.ChatScreenRoute
import com.laomuji1999.compose.feature.chat.chat.ChatScreenRoute.Companion.composeChatScreen
import com.laomuji1999.compose.feature.chat.chat.ChatScreenRoute.Companion.navigateToChatScreen
import com.laomuji1999.compose.feature.samples.date.DateScreenRoute.composeDateScreen
import com.laomuji1999.compose.feature.samples.date.DateScreenRoute.navigateToDateScreen
import com.laomuji1999.compose.feature.integrations.firebase.FirebaseScreenRoute.composeFirebaseScreen
import com.laomuji1999.compose.feature.integrations.firebase.FirebaseScreenRoute.navigateToFirebaseScreen
import com.laomuji1999.compose.feature.settings.font.FontScreenRoute
import com.laomuji1999.compose.feature.settings.font.FontScreenRoute.composeFontScreen
import com.laomuji1999.compose.feature.settings.font.FontScreenRoute.navigateToFontScreen
import com.laomuji1999.compose.feature.integrations.http.HttpScreenRoute.composeHttpScreen
import com.laomuji1999.compose.feature.integrations.http.HttpScreenRoute.navigateToHttpScreen
import com.laomuji1999.compose.feature.settings.language.LanguageScreenRoute
import com.laomuji1999.compose.feature.settings.language.LanguageScreenRoute.composeLanguageScreen
import com.laomuji1999.compose.feature.settings.language.LanguageScreenRoute.navigateToLanguageScreen
import com.laomuji1999.compose.feature.main.MainScreenRoute
import com.laomuji1999.compose.feature.main.MainScreenRoute.composeMainScreen
import com.laomuji1999.compose.feature.samples.painter.PainterScreenRoute.composePainterScreen
import com.laomuji1999.compose.feature.samples.painter.PainterScreenRoute.navigateToPainterScreen
import com.laomuji1999.compose.feature.samples.scroll.NestedScrollScreenRoute.composeNestedScrollScreen
import com.laomuji1999.compose.feature.samples.scroll.NestedScrollScreenRoute.navigateToNestedScrollScreen

/**
 * 导航控制器
 * 不同的feature之间的跳转
 */
@Composable
fun NavigationHost(
    navHostController: NavHostController,
    startDestination: Any = MainScreenRoute,
) {
    val activity = LocalActivity.current
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        enterTransition = SlideNavigation.enterTransition,
        exitTransition = SlideNavigation.exitTransition,
        popEnterTransition = SlideNavigation.popEnterTransition,
        popExitTransition = SlideNavigation.popExitTransition
    ) {
        composeMainScreen(
            navigateToGraph = { graph ->
                when (graph) {
                    MainScreenRoute.Graph.AiChat -> navHostController.navigateToAiChatScreen()
                    MainScreenRoute.Graph.Date -> navHostController.navigateToDateScreen()
                    MainScreenRoute.Graph.Firebase -> navHostController.navigateToFirebaseScreen()
                    MainScreenRoute.Graph.Http -> navHostController.navigateToHttpScreen()
                    MainScreenRoute.Graph.Language -> navHostController.navigateToLanguageScreen()
                    MainScreenRoute.Graph.Font -> navHostController.navigateToFontScreen()
                    MainScreenRoute.Graph.NestedScrollConnection -> navHostController.navigateToNestedScrollScreen()
                    MainScreenRoute.Graph.Painter -> navHostController.navigateToPainterScreen()
                    MainScreenRoute.Graph.WebView -> activity?.let { WebViewActivity.openWebViewActivity("https://www.baidu.com/", activity) }
                    is MainScreenRoute.Graph.VideoPlay -> activity?.let {
                        VideoPlayActivity.open(it, graph.url)
                    }
                }
            },
        )

        composeFirebaseScreen()

        composeHttpScreen()

        composeAiChatScreen(
            navigateToGraph = {
                when (it) {
                    is AiChatScreenRoute.Graph.Chat -> {
                        navHostController.navigateToChatScreen(
                            account = it.account
                        )
                    }
                }
            },
        )

        composeChatScreen(
            navigateToGraph = {
                when (it) {
                    ChatScreenRoute.Graph.Back -> navHostController.safePopBackStack()
                }
            },
        )

        composeDateScreen()

        composeNestedScrollScreen()

        composePainterScreen()

        composeLanguageScreen(
            navigateToGraph = {
                when (it) {
                    LanguageScreenRoute.Graph.Back -> navHostController.safePopBackStack()
                }
            },
        )

        composeFontScreen(
            navigateToGraph = {
                when (it) {
                    FontScreenRoute.Graph.Back -> navHostController.safePopBackStack()
                }
            },
        )
    }
}
