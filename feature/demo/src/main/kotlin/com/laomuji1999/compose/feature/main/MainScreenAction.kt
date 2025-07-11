package com.laomuji1999.compose.feature.main

sealed interface MainScreenAction {
    data object OnFirebaseClick : MainScreenAction
    data object OnHttpClick : MainScreenAction
    data object OnAiChatClick : MainScreenAction
    data object OnDateClick : MainScreenAction
    data object OnNestedScrollConnectionScreenClick : MainScreenAction
    data object OnBiometricScreenClick : MainScreenAction
    data object OnPainterScreenClick : MainScreenAction
    data object OnWebViewClick : MainScreenAction
    data object OnLanguageClick : MainScreenAction
    data object OnFontClick : MainScreenAction
}