package com.laomuji1999.compose.feature.settings.language

import com.laomuji1999.compose.core.logic.AppLanguages

data class LanguageScreenUiState(
    val usingLanguage: AppLanguages = AppLanguages.FlowSystem,
    val appLanguageList: List<AppLanguages> = AppLanguages.getAppLanguageList(),
)
