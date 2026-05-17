package com.laomuji1999.compose.feature.explore

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import com.laomuji1999.compose.feature.explore.firebase.FirebaseScreen
import com.laomuji1999.compose.feature.explore.http.HttpScreen
import kotlinx.serialization.Serializable

@Serializable
data object FirebaseScreenRoute : NavKey

@Serializable
data object HttpScreenRoute : NavKey

sealed interface ExploreScreenRouter {
    data object OnAiChatClick : ExploreScreenRouter
    data class OnVideoPlayClick(val url: String) : ExploreScreenRouter
    data object OnWebViewClick : ExploreScreenRouter
    data object OnHttpClick : ExploreScreenRouter
    data object OnFirebaseClick : ExploreScreenRouter
}

fun NavKey.provideExploreNavEntry(): NavEntry<NavKey>? {
    return when (val key = this) {
        is FirebaseScreenRoute -> NavEntry(key) { FirebaseScreen() }
        is HttpScreenRoute -> NavEntry(key) { HttpScreen() }
        else -> null
    }
}
