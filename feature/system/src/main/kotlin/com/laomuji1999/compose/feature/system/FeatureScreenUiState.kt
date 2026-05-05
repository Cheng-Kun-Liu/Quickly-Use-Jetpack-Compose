package com.laomuji1999.compose.feature.system

data class FeatureScreenUiState(
    val isLoading: Boolean = false,
    val enableSwitchAppLogo: Boolean = false,
    val location: String = "",
)
