package com.laomuji1999.compose.feature.settings.language

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object LanguageScreenRoute : NavKey {
    sealed interface Graph {
        data object Back : Graph
    }
}
