package com.laomuji1999.compose.feature.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.laomuji1999.compose.core.ui.navOptionsRemoveAll
import kotlinx.serialization.Serializable

@Serializable
data object MainScreenRoute {

    sealed interface Graph {
        data object Firebase : Graph
        data object Http : Graph
        data object AiChat : Graph
        data object Date : Graph
        data object NestedScrollConnection : Graph
        data object Biometric : Graph
        data object Painter : Graph
        data object WebView : Graph
        data object Language : Graph
        data object Font : Graph
    }

    fun NavHostController.navigateToMainScreen(
        route: MainScreenRoute, navOptions: NavOptions = navOptionsRemoveAll()
    ) {
        navigate(route = route, navOptions = navOptions)
    }

    fun NavGraphBuilder.composeMainScreen(
        navigateToGraph: (Graph) -> Unit,
    ) {
        composable<MainScreenRoute> {
            MainScreen(
                navigateToGraph = navigateToGraph
            )
        }
    }
}