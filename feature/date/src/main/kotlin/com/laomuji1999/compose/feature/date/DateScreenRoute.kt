package com.laomuji1999.compose.feature.date

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.laomuji1999.compose.core.ui.extension.navOptionsPushBack
import kotlinx.serialization.Serializable

@Serializable
data object DateScreenRoute{
    fun NavHostController.navigateToDateScreen(navOptions: NavOptions = navOptionsPushBack()){
        navigate(DateScreenRoute, navOptions)
    }

    fun NavGraphBuilder.composeDateScreen(){
        composable<DateScreenRoute>{
            DateScreen()
        }
    }
}