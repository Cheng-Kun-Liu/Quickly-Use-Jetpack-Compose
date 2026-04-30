package com.laomuji1999.compose.feature.integrations.biometric

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult

sealed interface BiometricScreenAction {
    data class HandleBiometric(val activityContext: Context) : BiometricScreenAction
    data class OnSettingClick(val biometricLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>) : BiometricScreenAction
    data class OnTitleChange(val title: String) : BiometricScreenAction
    data class OnDescriptionChange(val description: String) : BiometricScreenAction

}
