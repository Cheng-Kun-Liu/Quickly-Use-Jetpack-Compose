package com.laomuji1999.compose.feature.firebase

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.laomuji1999.compose.core.ui.extension.navOptionsPushBack
import kotlinx.serialization.Serializable

@Serializable
data object FirebaseScreenRoute{
    fun NavHostController.navigateToFirebaseScreen(navOptions: NavOptions = navOptionsPushBack()){
        navigate(FirebaseScreenRoute, navOptions)
    }

    fun NavGraphBuilder.composeFirebaseScreen(){
        composable<FirebaseScreenRoute>{
            FirebaseScreen()
        }
    }
}