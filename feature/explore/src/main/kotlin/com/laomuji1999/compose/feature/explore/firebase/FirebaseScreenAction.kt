package com.laomuji1999.compose.feature.explore.firebase

sealed interface FirebaseScreenAction {
    data object OnClickLogEvent : FirebaseScreenAction
    data object UpdatePushToken : FirebaseScreenAction
    data object TestCrashlytics : FirebaseScreenAction
}
