package com.laomuji1999.compose.feature.font

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laomuji1999.compose.core.ui.emitGraph
import com.laomuji1999.compose.core.ui.stateInTimeout
import com.laomuji1999.compose.core.ui.we.cache.WeCacheTypography
import com.laomuji1999.compose.core.ui.we.typography.WeTypography
import com.laomuji1999.compose.core.ui.we.typography.WeTypography13
import com.laomuji1999.compose.core.ui.we.typography.WeTypography14
import com.laomuji1999.compose.core.ui.we.typography.WeTypography15
import com.laomuji1999.compose.core.ui.we.typography.WeTypography16
import com.laomuji1999.compose.core.ui.we.typography.WeTypography17
import com.laomuji1999.compose.core.ui.we.typography.WeTypography18
import com.laomuji1999.compose.core.ui.we.typography.WeTypography19
import com.laomuji1999.compose.core.ui.we.typography.WeTypography20
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FontScreenViewModel @Inject constructor(
) : ViewModel() {
    private val _graph = MutableSharedFlow<FontScreenRoute.Graph>()
    val graph = _graph.asSharedFlow()

    private val allWeTypography = listOf(
        WeTypography13,
        WeTypography14,
        WeTypography15,
        WeTypography16,
        WeTypography17,
        WeTypography18,
        WeTypography19,
        WeTypography20,
    )
    private val _currentWeTypography: MutableStateFlow<WeTypography?> = MutableStateFlow(null)
    private val _currentIndex =
        MutableStateFlow(allWeTypography.indexOf(WeCacheTypography.currentWeTypography.value))

    val uiState = combine(
        _currentWeTypography,
        _currentIndex,
    ) { currentWeTypography, index ->
        FontScreenUiState(
            currentWeTypography = currentWeTypography,
            currentIndex = index,
            allWeTypography = allWeTypography,
        )
    }.stateInTimeout(viewModelScope, FontScreenUiState())


    fun onAction(action: FontScreenAction) {
        when (action) {
            FontScreenAction.OnClickBack -> _graph.emitGraph(FontScreenRoute.Graph.Back)
            is FontScreenAction.OnChangeTypography -> {
                _currentWeTypography.update {
                    allWeTypography[action.index]
                }
                _currentIndex.update {
                    action.index
                }
            }

            FontScreenAction.OnConfirm -> {
                _currentWeTypography.value?.let {
                    WeCacheTypography.setWeTypography(it)
                }
                _graph.emitGraph(FontScreenRoute.Graph.Back)
            }
        }
    }
}