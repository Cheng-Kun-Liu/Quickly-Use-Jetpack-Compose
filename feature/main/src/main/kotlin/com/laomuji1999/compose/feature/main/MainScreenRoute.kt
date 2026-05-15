package com.laomuji1999.compose.feature.main

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object MainScreenRoute : NavKey {

    sealed interface Graph {
        data object Firebase : Graph
        data object Http : Graph
        data object AiChat : Graph
        data object Date : Graph
        data object NestedScrollConnection : Graph
        data object Painter : Graph
        data object WebView : Graph
        data class VideoPlay(val url: String) : Graph
        data object Language : Graph
        data object Font : Graph
    }
}
