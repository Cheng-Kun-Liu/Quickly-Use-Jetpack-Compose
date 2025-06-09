package com.laomuji888.compose.feature.main

import androidx.lifecycle.ViewModel
import com.laomuji888.compose.core.ui.emitGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {
    private val _graph = MutableSharedFlow<MainScreenRoute.Graph>()
    val graph = _graph.asSharedFlow()

    fun onAction(action: MainScreenAction) {
        when (action) {
            MainScreenAction.OnAiChatClick -> _graph.emitGraph(MainScreenRoute.Graph.AiChat)
            MainScreenAction.OnBiometricScreenClick -> _graph.emitGraph(MainScreenRoute.Graph.Biometric)
            MainScreenAction.OnDateClick -> _graph.emitGraph(MainScreenRoute.Graph.Date)
            MainScreenAction.OnFirebaseClick -> _graph.emitGraph(MainScreenRoute.Graph.Firebase)
            MainScreenAction.OnHttpClick -> _graph.emitGraph(MainScreenRoute.Graph.Http)
            MainScreenAction.OnLanguageClick -> _graph.emitGraph(MainScreenRoute.Graph.Language)
            MainScreenAction.OnNestedScrollConnectionScreenClick -> _graph.emitGraph(MainScreenRoute.Graph.NestedScrollConnection)
            MainScreenAction.OnNestedScrollDispatcherScreenClick -> _graph.emitGraph(MainScreenRoute.Graph.NestedScrollDispatcher)
            MainScreenAction.OnPainterScreenClick -> _graph.emitGraph(MainScreenRoute.Graph.Painter)
            MainScreenAction.OnWebViewClick -> _graph.emitGraph(MainScreenRoute.Graph.WebView)
            MainScreenAction.OnYoutubeDLClick -> _graph.emitGraph(MainScreenRoute.Graph.YoutubeDL)
        }
    }
}