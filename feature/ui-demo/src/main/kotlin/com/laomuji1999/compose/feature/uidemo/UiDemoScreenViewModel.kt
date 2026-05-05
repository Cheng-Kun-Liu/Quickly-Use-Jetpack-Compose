package com.laomuji1999.compose.feature.uidemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laomuji1999.compose.core.ui.extension.stateInTimeout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UiDemoScreenViewModel @Inject constructor() : ViewModel() {
    private val _dragList = MutableStateFlow((1..20).map { "Item $it" })

    val uiState = _dragList.map { dragList ->
        UiDemoScreenUiState(
            dragList = dragList
        )
    }.stateInTimeout(viewModelScope, UiDemoScreenUiState())

    fun onAction(action: UiDemoScreenAction) {
        when (action) {
            is UiDemoScreenAction.SwapDragList -> {
                _dragList.update {
                    it.toMutableList().apply {
                        if (action.fromIndex in indices && action.toIndex in indices) {
                            val temp = this[action.fromIndex]
                            this[action.fromIndex] = this[action.toIndex]
                            this[action.toIndex] = temp
                        }
                    }
                }
            }
        }
    }
}
