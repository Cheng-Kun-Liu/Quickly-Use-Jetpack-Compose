package com.laomuji1999.compose.core.ui.we.animated

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * 从底部滑动起来的动画
 * @author laomuji666
 * @since 2025/5/23
 */
@Composable
fun AnimatedSlideFromBottom(
    state: AnimatedState,
    content: @Composable () -> Unit
) {
    val isShowAnimate by state.isShowAnimate.collectAsStateWithLifecycle()
    AnimatedVisibility(
        visible = isShowAnimate,
        enter = slideIn(
            animationSpec = tween(state.startDuration), initialOffset = {
                IntOffset(
                    x = 0, y = it.height
                )
            }), exit = slideOut(
            animationSpec = tween(state.endDuration),
            targetOffset = {
                IntOffset(
                    x = 0, y = it.height
                )
            },
        )
    ) {
        content()
    }
}