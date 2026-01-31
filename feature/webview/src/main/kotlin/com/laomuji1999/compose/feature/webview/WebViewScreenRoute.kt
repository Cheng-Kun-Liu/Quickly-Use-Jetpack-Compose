package com.laomuji1999.compose.feature.webview

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.laomuji1999.compose.core.ui.extension.navOptionsPushBack
import kotlinx.serialization.Serializable

@Serializable
data class WebViewScreenRoute(
    val url:String
){
    companion object{
        fun NavHostController.navigateToWebViewScreen(url: String, navOptions: NavOptions = navOptionsPushBack()){
            navigate(WebViewScreenRoute(url = url), navOptions)
        }

        fun NavGraphBuilder.composeWebViewScreen(
            onBackClick: () -> Unit,
            onOpenNewWindow: (String)->Unit,
        ){
            composable<WebViewScreenRoute>{
                WebViewScreen(
                    onBackClick = onBackClick,
                    onOpenNewWindow = onOpenNewWindow,
                )
            }
        }
    }
}