package com.laomuji1999.compose.feature.painter

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laomuji1999.compose.core.ui.extension.stateInTimeout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PainterScreenViewModel @Inject constructor() : ViewModel() {
    private val _colorList = listOf(
        Color(0xFFFF0000),
        Color(0xFFFFA500),
        Color(0xFFFFFF00),
        Color(0xFF00FF00),
        Color(0xFF00FFFF),
        Color(0xFF0000FF),
        Color(0xFF800080)
    )
    private val _widthList = listOf(
        1.dp,
        2.5.dp,
        4.dp,
        5.5.dp,
        8.dp,
        12.dp,
        15.5.dp,
        20.dp
    )
    private val _currentColorIndex = MutableStateFlow(0)
    private val _currentWidthIndex = MutableStateFlow(0)
    private val _pathList = MutableStateFlow(emptyList<PathData>())
    private val _currentPath:MutableStateFlow<PathData?> = MutableStateFlow(null)

    val uiState = combine(
        _currentColorIndex,
        _currentWidthIndex,
        _pathList,
        _currentPath
    ){ currentColorIndex, currentWidthIndex, pathList, currentPath ->
        PainterScreenUiState(
            colorList = _colorList,
            currentColorIndex = currentColorIndex,
            widthList = _widthList,
            currentWidthIndex = currentWidthIndex,
            pathList = pathList,
            currentPath = currentPath
        )
    }.stateInTimeout(viewModelScope, PainterScreenUiState())

    fun onAction(action: PainterScreenAction){
        when(action){
            PainterScreenAction.OnClearCanvas -> {
                _pathList.value = emptyList()
            }
            is PainterScreenAction.OnCurrentColorIndexChange -> {
                _currentColorIndex.value = action.index
            }
            is PainterScreenAction.OnCurrentWidthIndexChange -> {
                _currentWidthIndex.value = action.index
            }
            is PainterScreenAction.OnDragStart -> {
                _currentPath.value = PathData(
                    color = _colorList[_currentColorIndex.value],
                    path = listOf(action.offset),
                    width = _widthList[_currentWidthIndex.value]
                )
            }
            PainterScreenAction.OnDragEnd -> {
                _pathList.update {
                    it + _currentPath.value!!
                }
                _currentPath.value = null
            }
            is PainterScreenAction.OnDrag -> {
                _currentPath.value.apply {
                    _currentPath.update {
                        it!!.copy(
                            path = it.path + action.offset
                        )
                    }
                }
            }
        }
    }
}