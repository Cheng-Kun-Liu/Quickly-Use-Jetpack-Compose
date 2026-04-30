package com.laomuji1999.compose.feature.main.ui

sealed interface UiDemoScreenAction {
    data class SwapDragList(val a:Int,val b:Int): UiDemoScreenAction
}
