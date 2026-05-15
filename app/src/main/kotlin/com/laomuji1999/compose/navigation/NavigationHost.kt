package com.laomuji1999.compose.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.result.rememberResultEventBusNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.laomuji1999.compose.core.logic.common.Log
import com.laomuji1999.compose.core.ui.extension.nav3PopBackStack
import com.laomuji1999.compose.core.ui.screen.SlideNavigation
import com.laomuji1999.compose.feature.chat.AiChatScreen
import com.laomuji1999.compose.feature.chat.AiChatScreenRoute
import com.laomuji1999.compose.feature.chat.chat.ChatScreen
import com.laomuji1999.compose.feature.chat.chat.ChatScreenRoute
import com.laomuji1999.compose.feature.explore.firebase.FirebaseScreen
import com.laomuji1999.compose.feature.explore.firebase.FirebaseScreenRoute
import com.laomuji1999.compose.feature.explore.http.HttpScreen
import com.laomuji1999.compose.feature.explore.http.HttpScreenRoute
import com.laomuji1999.compose.feature.main.MainScreen
import com.laomuji1999.compose.feature.main.MainScreenRoute
import com.laomuji1999.compose.feature.settings.font.FontScreen
import com.laomuji1999.compose.feature.settings.font.FontScreenRoute
import com.laomuji1999.compose.feature.settings.language.LanguageScreen
import com.laomuji1999.compose.feature.settings.language.LanguageScreenRoute
import com.laomuji1999.compose.feature.uidemo.date.DateScreen
import com.laomuji1999.compose.feature.uidemo.date.DateScreenRoute
import com.laomuji1999.compose.feature.uidemo.painter.PainterScreen
import com.laomuji1999.compose.feature.uidemo.painter.PainterScreenRoute
import com.laomuji1999.compose.feature.uidemo.scroll.NestedScrollScreen
import com.laomuji1999.compose.feature.uidemo.scroll.NestedScrollScreenRoute
import com.laomuji1999.compose.feature.video.VideoPlayActivity
import com.laomuji1999.compose.feature.webview.WebViewActivity

/**
 * 导航控制器
 * 不同的feature之间的跳转
 */
@Composable
fun NavigationHost(
    backStack: MutableList<NavKey>,
) {
    val activity = LocalActivity.current
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.nav3PopBackStack() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberResultEventBusNavEntryDecorator(),
            rememberLoggingNavEntryDecorator()
        ),
        transitionSpec = SlideNavigation.nav3TransitionSpec,
        popTransitionSpec = SlideNavigation.nav3PopTransitionSpec,
        predictivePopTransitionSpec = SlideNavigation.nav3PredictivePopTransitionSpec
    ) { key ->
        when (key) {
            is MainScreenRoute -> NavEntry(key) {
                MainScreen(
                    navigateToGraph = { graph ->
                        when (graph) {
                            MainScreenRoute.Graph.AiChat -> backStack.add(AiChatScreenRoute)
                            MainScreenRoute.Graph.Date -> backStack.add(DateScreenRoute)
                            MainScreenRoute.Graph.Firebase -> backStack.add(FirebaseScreenRoute)
                            MainScreenRoute.Graph.Http -> backStack.add(HttpScreenRoute)
                            MainScreenRoute.Graph.Language -> backStack.add(LanguageScreenRoute)
                            MainScreenRoute.Graph.Font -> backStack.add(FontScreenRoute)
                            MainScreenRoute.Graph.NestedScrollConnection -> backStack.add(
                                NestedScrollScreenRoute
                            )

                            MainScreenRoute.Graph.Painter -> backStack.add(PainterScreenRoute)
                            MainScreenRoute.Graph.WebView -> activity?.let {
                                WebViewActivity.openWebViewActivity(
                                    "https://www.baidu.com/",
                                    activity
                                )
                            }

                            is MainScreenRoute.Graph.VideoPlay -> activity?.let {
                                VideoPlayActivity.open(it, graph.url)
                            }
                        }
                    },
                )
            }

            is FirebaseScreenRoute -> NavEntry(key) {
                FirebaseScreen()
            }

            is HttpScreenRoute -> NavEntry(key) {
                HttpScreen()
            }

            is AiChatScreenRoute -> NavEntry(key) {
                AiChatScreen(
                    navigateToGraph = {
                        when (it) {
                            is AiChatScreenRoute.Graph.Chat -> {
                                backStack.add(ChatScreenRoute(it.account))
                            }
                        }
                    },
                )
            }

            is ChatScreenRoute -> NavEntry(key) { route ->
                ChatScreen(
                    account = (route as ChatScreenRoute).account,
                    navigateToGraph = {
                        when (it) {
                            ChatScreenRoute.Graph.Back -> backStack.nav3PopBackStack()
                        }
                    },
                )
            }

            is DateScreenRoute -> NavEntry(key) {
                DateScreen()
            }

            is NestedScrollScreenRoute -> NavEntry(key) {
                NestedScrollScreen()
            }

            is PainterScreenRoute -> NavEntry(key) {
                PainterScreen()
            }

            is LanguageScreenRoute -> NavEntry(key) {
                LanguageScreen(
                    navigateToGraph = {
                        when (it) {
                            LanguageScreenRoute.Graph.Back -> backStack.nav3PopBackStack()
                        }
                    },
                )
            }

            is FontScreenRoute -> NavEntry(key) {
                FontScreen(
                    navigateToGraph = {
                        when (it) {
                            FontScreenRoute.Graph.Back -> backStack.nav3PopBackStack()
                        }
                    },
                )
            }

            else -> NavEntry(key) {}
        }
    }
}

@Composable
private fun <T : Any> rememberLoggingNavEntryDecorator(): NavEntryDecorator<T> =
    NavEntryDecorator { entry ->
        LaunchedEffect(entry.contentKey) {
            Log.debug("tag_NavigationHost", "key: ${entry.contentKey}")
        }
        entry.Content()
    }
