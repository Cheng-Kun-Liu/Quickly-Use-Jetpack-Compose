package com.laomuji1999.compose.feature.uidemo

sealed interface UiDemoScreenAction {
    data class SwapDragList(val fromIndex: Int, val toIndex: Int) : UiDemoScreenAction
}
