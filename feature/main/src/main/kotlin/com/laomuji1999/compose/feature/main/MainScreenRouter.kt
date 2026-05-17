package com.laomuji1999.compose.feature.main

import android.app.Activity
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import com.laomuji1999.compose.feature.chat.AiChatScreenRoute
import com.laomuji1999.compose.feature.explore.ExploreScreenRouter
import com.laomuji1999.compose.feature.explore.FirebaseScreenRoute
import com.laomuji1999.compose.feature.explore.HttpScreenRoute
import com.laomuji1999.compose.feature.settings.FontScreenRoute
import com.laomuji1999.compose.feature.settings.LanguageScreenRoute
import com.laomuji1999.compose.feature.settings.SettingsScreenRouter
import com.laomuji1999.compose.feature.uidemo.DateScreenRoute
import com.laomuji1999.compose.feature.uidemo.NestedScrollScreenRoute
import com.laomuji1999.compose.feature.uidemo.PainterScreenRoute
import com.laomuji1999.compose.feature.uidemo.UiDemoScreenRouter
import com.laomuji1999.compose.feature.video.VideoPlayActivity
import com.laomuji1999.compose.feature.webview.WebViewActivity
import kotlinx.serialization.Serializable

@Serializable
data object MainScreenRoute : NavKey {
    sealed interface Graph {
        data object Firebase : Graph
        data object Http : Graph
        data object AiChat : Graph
        data object Date : Graph
        data object NestedScrollConnection : Graph
        data object Painter : Graph
        data object WebView : Graph
        data class VideoPlay(val url: String) : Graph
        data object Language : Graph
        data object Font : Graph
    }
}

sealed interface MainScreenRouter {
    data class ExploreRouter(val router: ExploreScreenRouter) : MainScreenRouter
    data class UiDemoRouter(val router: UiDemoScreenRouter) : MainScreenRouter
    data class SettingsRouter(val router: SettingsScreenRouter) : MainScreenRouter
}

fun NavKey.provideMainNavEntry(
    backStack: MutableList<NavKey>,
    activity: Activity?
): NavEntry<NavKey>? {
    return when (val key = this) {
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

        else -> null
    }
}
