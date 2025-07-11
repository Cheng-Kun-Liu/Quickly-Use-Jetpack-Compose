package com.laomuji1999.compose.feature.language

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.laomuji1999.compose.core.ui.navOptionsPushBack
import kotlinx.serialization.Serializable

@Serializable
data object LanguageScreenRoute {

    sealed interface Graph {
        data object Back : Graph
    }

    fun NavHostController.navigateToLanguageScreen(
        navOptions: NavOptions = navOptionsPushBack()
    ) {
        navigate(route = LanguageScreenRoute, navOptions = navOptions)
    }

    fun NavGraphBuilder.composeLanguageScreen(
        navigateToGraph: (Graph) -> Unit,
    ) {
        composable<LanguageScreenRoute> {
            LanguageScreen(
                navigateToGraph = navigateToGraph
            )
        }
    }
}