package com.laomuji1999.compose.core.ui.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.navigation3.scene.Scene

/**
 * 不同屏幕间的切换,使用滑动进入滑动退出的效果
 * @author laomuji666
 * @since 2025/5/23
 */
class SlideNavigation {
    companion object {
        private const val ANIM_TIME = 350

        fun <T : Any> nav3TransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(ANIM_TIME)
            ) togetherWith slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(ANIM_TIME)
            )
        }

        fun <T : Any> nav3PopTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(ANIM_TIME)
            ) togetherWith slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(ANIM_TIME)
            )
        }

        fun <T : Any> nav3PredictivePopTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.(Int) -> ContentTransform = { _ ->
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(ANIM_TIME)
            ) togetherWith slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(ANIM_TIME)
            )
        }
    }
}
