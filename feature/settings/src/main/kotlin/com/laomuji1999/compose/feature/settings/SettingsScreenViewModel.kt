package com.laomuji1999.compose.feature.settings

import androidx.lifecycle.ViewModel
import com.laomuji1999.compose.core.logic.AppLanguages
import com.laomuji1999.compose.core.logic.Language
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val language: Language
) : ViewModel() {
    private val _usingLanguage = MutableStateFlow(language.getAppUsingLanguage())
    val usingLanguage = _usingLanguage.asStateFlow()

    fun updateLanguage(appLanguages: AppLanguages) {
        _usingLanguage.update { appLanguages }
    }
}
