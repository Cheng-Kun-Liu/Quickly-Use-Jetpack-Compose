package com.laomuji1999.compose.feature.uidemo

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import com.laomuji1999.compose.feature.uidemo.date.DateScreen
import com.laomuji1999.compose.feature.uidemo.painter.PainterScreen
import com.laomuji1999.compose.feature.uidemo.scroll.NestedScrollScreen
import kotlinx.serialization.Serializable

@Serializable
data object DateScreenRoute : NavKey

@Serializable
data object PainterScreenRoute : NavKey

@Serializable
data object NestedScrollScreenRoute : NavKey

sealed interface UiDemoScreenRouter {
    data object OnDateClick : UiDemoScreenRouter
    data object OnNestedScrollConnectionScreenClick : UiDemoScreenRouter
    data object OnPainterScreenClick : UiDemoScreenRouter
}

fun NavKey.provideUiDemoNavEntry(): NavEntry<NavKey>? {
    return when (val key = this) {
        is DateScreenRoute -> NavEntry(key) { DateScreen() }
        is NestedScrollScreenRoute -> NavEntry(key) { NestedScrollScreen() }
        is PainterScreenRoute -> NavEntry(key) { PainterScreen() }
        else -> null
    }
}
