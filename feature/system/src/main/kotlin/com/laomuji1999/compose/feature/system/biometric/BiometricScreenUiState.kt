package com.laomuji1999.compose.feature.system.biometric

import com.laomuji1999.compose.core.logic.authenticate.biometric.BiometricAuthenticate

data class BiometricScreenUiState(
    val title: String = "",
    val description: String = "",
    val biometricResult: BiometricAuthenticate.BiometricAuthenticateResult? = null,
)
