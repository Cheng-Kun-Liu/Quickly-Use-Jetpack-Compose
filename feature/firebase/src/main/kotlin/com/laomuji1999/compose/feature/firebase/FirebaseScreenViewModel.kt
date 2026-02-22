package com.laomuji1999.compose.feature.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laomuji1999.compose.core.logic.notification.FirebaseNotification
import com.laomuji1999.compose.core.ui.extension.stateInTimeout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class FirebaseScreenViewModel @Inject constructor(
    private val analytics: com.laomuji1999.compose.core.logic.analytics.Analytics,
    private val notification: FirebaseNotification
) : ViewModel() {
    private val _pushToken = MutableStateFlow("")
    val uiState = _pushToken.map {
        FirebaseScreenUiState(
            pushToken = it
        )
    }.stateInTimeout(viewModelScope, FirebaseScreenUiState())

    fun onAction(action: FirebaseScreenAction){
        when(action){
            FirebaseScreenAction.OnClickLogEvent -> logEventClick()
            FirebaseScreenAction.UpdatePushToken -> updatePushToken()
            FirebaseScreenAction.TestCrashlytics -> testCrashlytics()
        }
    }

    private fun logEventClick(){
        analytics.logEvent("HelloScreen_logEventClick")
    }

    private fun updatePushToken() {
        notification.updatePushToken {
            _pushToken.value = it
        }
    }

    private fun testCrashlytics() {
        throw RuntimeException("Test Crash")
    }
}