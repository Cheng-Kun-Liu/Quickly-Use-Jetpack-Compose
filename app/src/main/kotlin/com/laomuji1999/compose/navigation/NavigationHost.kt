package com.laomuji1999.compose.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.result.rememberResultEventBusNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.laomuji1999.compose.core.logic.common.Log
import com.laomuji1999.compose.core.ui.extension.nav3PopBackStack
import com.laomuji1999.compose.core.ui.screen.SlideNavigation
import com.laomuji1999.compose.feature.chat.provideChatNavEntry
import com.laomuji1999.compose.feature.explore.provideExploreNavEntry
import com.laomuji1999.compose.feature.main.provideMainNavEntry
import com.laomuji1999.compose.feature.settings.provideSettingsNavEntry
import com.laomuji1999.compose.feature.uidemo.provideUiDemoNavEntry

/**
 * 导航控制器
 * 采用分布式路由分发模式
 */
@Composable
fun NavigationHost(
    backStack: MutableList<NavKey>,
) {
    val activity = LocalActivity.current
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.nav3PopBackStack() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberResultEventBusNavEntryDecorator(),
            rememberLoggingNavEntryDecorator()
        ),
        transitionSpec = SlideNavigation.nav3TransitionSpec(),
        popTransitionSpec = SlideNavigation.nav3PopTransitionSpec(),
        predictivePopTransitionSpec = SlideNavigation.nav3PredictivePopTransitionSpec()
    ) { key ->
        // 依次询问各个模块是否能处理该路由
        key.provideMainNavEntry(backStack, activity)
            ?: key.provideChatNavEntry(backStack)
            ?: key.provideExploreNavEntry()
            ?: key.provideSettingsNavEntry(backStack)
            ?: key.provideUiDemoNavEntry()
            ?: NavEntry(key) {}
    }
}

@Composable
private fun <T : Any> rememberLoggingNavEntryDecorator(): NavEntryDecorator<T> =
    NavEntryDecorator { entry ->
        LaunchedEffect(entry.contentKey) {
            Log.debug("tag_NavigationHost", "key: ${entry.contentKey}")
        }
        entry.Content()
    }
