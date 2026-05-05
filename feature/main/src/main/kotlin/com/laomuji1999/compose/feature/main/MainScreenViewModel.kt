package com.laomuji1999.compose.feature.main

import androidx.lifecycle.ViewModel
import com.laomuji1999.compose.core.ui.extension.emitGraph
import com.laomuji1999.compose.core.ui.navigation.AppNavigationAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {
    private val _graph = MutableSharedFlow<MainScreenRoute.Graph>()
    val graph = _graph.asSharedFlow()

    fun onAction(action: AppNavigationAction) {
        when (action) {
            AppNavigationAction.OnAiChatClick -> _graph.emitGraph(MainScreenRoute.Graph.AiChat)
            AppNavigationAction.OnDateClick -> _graph.emitGraph(MainScreenRoute.Graph.Date)
            AppNavigationAction.OnFirebaseClick -> _graph.emitGraph(MainScreenRoute.Graph.Firebase)
            AppNavigationAction.OnHttpClick -> _graph.emitGraph(MainScreenRoute.Graph.Http)
            AppNavigationAction.OnLanguageClick -> _graph.emitGraph(MainScreenRoute.Graph.Language)
            AppNavigationAction.OnFontClick -> _graph.emitGraph(MainScreenRoute.Graph.Font)
            AppNavigationAction.OnNestedScrollConnectionScreenClick -> _graph.emitGraph(MainScreenRoute.Graph.NestedScrollConnection)
            AppNavigationAction.OnPainterScreenClick -> _graph.emitGraph(MainScreenRoute.Graph.Painter)
            AppNavigationAction.OnWebViewClick -> _graph.emitGraph(MainScreenRoute.Graph.WebView)
            is AppNavigationAction.OnVideoPlayClick -> _graph.emitGraph(MainScreenRoute.Graph.VideoPlay(action.url))
        }
    }
}
