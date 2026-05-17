package com.laomuji1999.compose.feature.settings

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import com.laomuji1999.compose.core.ui.extension.nav3PopBackStack
import com.laomuji1999.compose.feature.settings.font.FontScreen
import com.laomuji1999.compose.feature.settings.language.LanguageScreen
import kotlinx.serialization.Serializable

@Serializable
data object LanguageScreenRoute : NavKey {
    sealed interface Graph {
        data object Back : Graph
    }
}

@Serializable
data object FontScreenRoute : NavKey {
    sealed interface Graph {
        data object Back : Graph
    }
}

sealed interface SettingsScreenRouter {
    data object OnLanguageClick : SettingsScreenRouter
    data object OnFontClick : SettingsScreenRouter
}

fun NavKey.provideSettingsNavEntry(
    backStack: MutableList<NavKey>
): NavEntry<NavKey>? {
    return when (val key = this) {
        is LanguageScreenRoute -> NavEntry(key) {
            LanguageScreen(
                navigateToGraph = { graph ->
                    when (graph) {
                        LanguageScreenRoute.Graph.Back -> backStack.nav3PopBackStack()
                    }
                }
            )
        }

        is FontScreenRoute -> NavEntry(key) {
            FontScreen(
                navigateToGraph = { graph ->
                    when (graph) {
                        FontScreenRoute.Graph.Back -> backStack.nav3PopBackStack()
                    }
                }
            )
        }

        else -> null
    }
}
