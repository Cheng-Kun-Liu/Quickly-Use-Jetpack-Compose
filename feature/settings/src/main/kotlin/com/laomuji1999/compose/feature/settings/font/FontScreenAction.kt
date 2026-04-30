package com.laomuji1999.compose.feature.settings.font

sealed interface FontScreenAction {
    data object OnClickBack : FontScreenAction
    data class OnChangeTypography(val index: Int) : FontScreenAction
    data object OnConfirm : FontScreenAction
}
