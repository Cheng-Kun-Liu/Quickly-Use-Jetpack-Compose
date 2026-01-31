package com.laomuji1999.compose.feature.painter

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.laomuji1999.compose.core.ui.extension.navOptionsPushBack
import kotlinx.serialization.Serializable

@Serializable
data object PainterScreenRoute{
    fun NavHostController.navigateToPainterScreen(navOptions: NavOptions = navOptionsPushBack()){
        navigate(PainterScreenRoute, navOptions)
    }

    fun NavGraphBuilder.composePainterScreen(){
        composable<PainterScreenRoute>{
            PainterScreen()
        }
    }
}

