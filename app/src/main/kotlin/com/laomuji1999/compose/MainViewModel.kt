package com.laomuji1999.compose

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laomuji1999.compose.core.logic.common.Log
import com.laomuji1999.compose.core.ui.stateInTimeout
import com.laomuji1999.compose.flavor.FlavorDemo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/**
 * [HiltViewModel],[Inject] 注入ViewModel
 */
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    /**
     * MainActivity的状态
     * 默认[MainUiState.Loading]
     */
    private val _mainUiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState = _mainUiState.stateInTimeout(
        viewModelScope,
        MainUiState.Loading
    )

    /**
     * 初始化三方SDK,然后标记成功状态.
     */
    fun initModule(activity: Activity) {
        Log.debug("tag_flavor", FlavorDemo().getFlavor())
        _mainUiState.value = MainUiState.Success
    }
}

/**
 * MainActivity的状态
 * 只会是[MainUiState.Loading],[MainUiState.Success]
 */
sealed interface MainUiState {
    data object Loading : MainUiState
    data object Success : MainUiState
}