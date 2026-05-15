package com.laomuji1999.compose.feature.webview

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class WebViewScreenRoute(
    val url:String
) : NavKey
