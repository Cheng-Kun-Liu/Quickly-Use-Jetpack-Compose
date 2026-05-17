package com.laomuji1999.compose.feature.webview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laomuji1999.compose.core.ui.extension.stateInTimeout
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

@HiltViewModel(assistedFactory = WebViewScreenViewModel.Factory::class)
class WebViewScreenViewModel @AssistedInject constructor(
    @Assisted private val route: WebViewScreenRoute,
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(route: WebViewScreenRoute): WebViewScreenViewModel
    }

    private val _url = MutableStateFlow(route.url)
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