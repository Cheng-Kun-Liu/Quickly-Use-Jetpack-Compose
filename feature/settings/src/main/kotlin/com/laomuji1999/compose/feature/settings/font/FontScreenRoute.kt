package com.laomuji1999.compose.feature.settings.font

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object FontScreenRoute : NavKey {
    sealed interface Graph {
        data object Back : Graph
    }
}
