package com.laomuji1999.compose.feature.language

import android.content.Context
import com.laomuji1999.compose.core.logic.AppLanguages

sealed interface LanguageScreenAction {
    data object OnClickBack : LanguageScreenAction
    data class OnLanguageClick(val appLanguage: AppLanguages, val context: Context) : LanguageScreenAction
}