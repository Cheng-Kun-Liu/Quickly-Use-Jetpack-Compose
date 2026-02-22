package com.laomuji1999.compose.feature.webview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.laomuji1999.compose.core.ui.extension.stateInTimeout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class WebViewScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _url = MutableStateFlow((savedStateHandle.toRoute<WebViewScreenRoute>()).url)
    private val _title = MutableStateFlow("")
    private val _progress = MutableStateFlow(0)

    val uiState = combine(
        _url,
        _title,
        _progress
    ){ url, title, progress ->
        WebViewScreenUiState(
            url = url,
            title = title,
            progress = progress
        )
    }.stateInTimeout(viewModelScope, WebViewScreenUiState())

    fun onAction(action: WebViewScreenAction){
        when(action){
            is WebViewScreenAction.OnProgressChanged -> {
                _progress.value = action.progress
            }
            is WebViewScreenAction.OnReceivedTitle -> {
                _title.value = action.title ?: ""
            }
            is WebViewScreenAction.OnUrlChanged -> {
                _url.value = action.url
            }
        }
    }
}