package com.laomuji1999.compose.feature.scroll

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.laomuji1999.compose.core.ui.extension.navOptionsPushBack
import kotlinx.serialization.Serializable

@Serializable
data object NestedScrollScreenRoute {
    fun NavHostController.navigateToNestedScrollScreen(navOptions: NavOptions = navOptionsPushBack()) {
        navigate(NestedScrollScreenRoute, navOptions)
    }

    fun NavGraphBuilder.composeNestedScrollScreen() {
        composable<NestedScrollScreenRoute> {
            NestedScrollScreen()
        }
    }
}