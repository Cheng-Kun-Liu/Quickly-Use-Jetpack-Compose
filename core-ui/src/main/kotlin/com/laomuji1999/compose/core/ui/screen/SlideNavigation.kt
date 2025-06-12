package com.laomuji1999.compose.core.ui.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

/**
 * 不同屏幕间的切换,使用滑动进入滑动退出的效果
 * @author laomuji666
 * @since 2025/5/23
 */
class SlideNavigation {
    companion object {
        private const val ANIM_TIME = 350

        val enterTransition:
                (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
            {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(ANIM_TIME)
                )
            }

        val exitTransition:
                (@JvmSuppressWildcards
                AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
            {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(ANIM_TIME)
                )
            }

        val popEnterTransition:
                (@JvmSuppressWildcards
                AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
            {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(ANIM_TIME)
                )
            }

        val popExitTransition:
                (@JvmSuppressWildcards
                AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
            {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(ANIM_TIME)
                )
            }
    }
}