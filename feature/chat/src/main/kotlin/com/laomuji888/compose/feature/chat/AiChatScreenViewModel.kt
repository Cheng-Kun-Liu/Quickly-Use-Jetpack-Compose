package com.laomuji888.compose.feature.chat

import androidx.lifecycle.ViewModel
import com.laomuji888.compose.core.ui.emitGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class AiChatScreenViewModel @Inject constructor() : ViewModel() {
    private val _graph = MutableSharedFlow<AiChatScreenRoute.Graph>()
    val graph = _graph.asSharedFlow()

    fun onAction(action: AiChatScreenAction) {
        when (action) {
            is AiChatScreenAction.OnClickContact -> _graph.emitGraph(
                AiChatScreenRoute.Graph.Chat(
                    account = action.account
                )
            )
        }
    }
}