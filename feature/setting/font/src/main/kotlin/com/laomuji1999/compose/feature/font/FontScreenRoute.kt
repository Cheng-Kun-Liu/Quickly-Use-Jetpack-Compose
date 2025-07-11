package com.laomuji1999.compose.feature.font

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.laomuji1999.compose.core.ui.navOptionsPushBack
import kotlinx.serialization.Serializable

@Serializable
data object FontScreenRoute {

    sealed interface Graph {
        data object Back : Graph
    }

    fun NavHostController.navigateToFontScreen(
        navOptions: NavOptions = navOptionsPushBack()
    ) {
        navigate(route = FontScreenRoute, navOptions = navOptions)
    }

    fun NavGraphBuilder.composeFontScreen(
        navigateToGraph: (Graph) -> Unit,
    ) {
        composable<FontScreenRoute> {
            FontScreen(
                navigateToGraph = navigateToGraph
            )
        }
    }
}