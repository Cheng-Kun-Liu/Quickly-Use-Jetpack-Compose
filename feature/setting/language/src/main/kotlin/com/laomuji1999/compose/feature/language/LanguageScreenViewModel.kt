package com.laomuji1999.compose.feature.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laomuji1999.compose.core.logic.AppLanguages
import com.laomuji1999.compose.core.logic.Language
import com.laomuji1999.compose.core.ui.extension.emitGraph
import com.laomuji1999.compose.core.ui.extension.stateInTimeout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageScreenViewModel @Inject constructor(
    private val language: Language
) : ViewModel() {
    private val _graph = MutableSharedFlow<LanguageScreenRoute.Graph>()
    val graph = _graph.asSharedFlow()

    private val _usingLanguage = MutableStateFlow(language.getAppUsingLanguage())
    private val _appLanguageList = MutableStateFlow(AppLanguages.getAppLanguageList())

    val uiState = combine(
        _usingLanguage,
        _appLanguageList,
    ) { usingLanguage, appLanguageList ->
        LanguageScreenUiState(
            usingLanguage = usingLanguage,
            appLanguageList = appLanguageList,
        )
    }.stateInTimeout(viewModelScope, LanguageScreenUiState())

    fun onAction(
        action: LanguageScreenAction,
    ) {
        when (action) {
            is LanguageScreenAction.OnLanguageClick -> {
                if (action.appLanguage == AppLanguages.FlowSystem && language.getSystemLanguagesHandleIntent() != null) {
                    action.context.startActivity(language.getSystemLanguagesHandleIntent())
                    viewModelScope.launch {
                        delay(1000)
                        setAppLanguage(action)
                    }
                } else {
                    setAppLanguage(action)
                }
            }

            LanguageScreenAction.OnClickBack -> _graph.emitGraph(LanguageScreenRoute.Graph.Back)
        }
    }

    private fun setAppLanguage(action: LanguageScreenAction.OnLanguageClick) {
        _usingLanguage.update {
            action.appLanguage
        }

        language.setAppUsingLanguage(
            appLanguage = action.appLanguage
        )
    }

    fun updateLanguageStatus() {
        _usingLanguage.update {
            language.getAppUsingLanguage()
        }
        _appLanguageList.update {
            AppLanguages.getAppLanguageList()
        }
    }
}