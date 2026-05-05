package com.laomuji1999.compose.core.ui.navigation

sealed interface AppNavigationAction {
    data object OnFirebaseClick : AppNavigationAction
    data object OnHttpClick : AppNavigationAction
    data object OnAiChatClick : AppNavigationAction
    data object OnDateClick : AppNavigationAction
    data object OnNestedScrollConnectionScreenClick : AppNavigationAction
    data object OnPainterScreenClick : AppNavigationAction
    data object OnWebViewClick : AppNavigationAction
    data class OnVideoPlayClick(val url: String) : AppNavigationAction
    data object OnLanguageClick : AppNavigationAction
    data object OnFontClick : AppNavigationAction
}
