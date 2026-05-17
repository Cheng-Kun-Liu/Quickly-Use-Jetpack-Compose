package com.laomuji1999.compose.feature.main

import androidx.lifecycle.ViewModel
import com.laomuji1999.compose.core.ui.extension.emitGraph
import com.laomuji1999.compose.feature.explore.ExploreScreenRouter
import com.laomuji1999.compose.feature.settings.SettingsScreenRouter
import com.laomuji1999.compose.feature.uidemo.UiDemoScreenRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {
    private val _graph = MutableSharedFlow<MainScreenRoute.Graph>()
    val graph = _graph.asSharedFlow()

    fun onAction(action: MainScreenRouter) {
        when (action) {
            is MainScreenRouter.ExploreRouter -> when (val exploreAction = action.router) {
                ExploreScreenRouter.OnAiChatClick -> _graph.emitGraph(MainScreenRoute.Graph.AiChat)
                ExploreScreenRouter.OnFirebaseClick -> _graph.emitGraph(MainScreenRoute.Graph.Firebase)
                ExploreScreenRouter.OnHttpClick -> _graph.emitGraph(MainScreenRoute.Graph.Http)
                ExploreScreenRouter.OnWebViewClick -> _graph.emitGraph(MainScreenRoute.Graph.WebView)
                is ExploreScreenRouter.OnVideoPlayClick -> _graph.emitGraph(
                    MainScreenRoute.Graph.VideoPlay(exploreAction.url)
                )
            }

            is MainScreenRouter.SettingsRouter -> when (action.router) {
                SettingsScreenRouter.OnFontClick -> _graph.emitGraph(MainScreenRoute.Graph.Font)
                SettingsScreenRouter.OnLanguageClick -> _graph.emitGraph(MainScreenRoute.Graph.Language)
            }

            is MainScreenRouter.UiDemoRouter -> when (action.router) {
                UiDemoScreenRouter.OnDateClick -> _graph.emitGraph(MainScreenRoute.Graph.Date)
                UiDemoScreenRouter.OnNestedScrollConnectionScreenClick -> _graph.emitGraph(
                    MainScreenRoute.Graph.NestedScrollConnection
                )

                UiDemoScreenRouter.OnPainterScreenClick -> _graph.emitGraph(MainScreenRoute.Graph.Painter)
            }
        }
    }
}
