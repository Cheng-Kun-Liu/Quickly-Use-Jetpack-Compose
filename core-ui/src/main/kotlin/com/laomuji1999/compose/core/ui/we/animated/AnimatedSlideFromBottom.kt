package com.laomuji1999.compose.core.ui.we.animated

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 从底部滑动起来的动画
 * @author laomuji666
 * @since 2025/5/23
 */
interface AnimatedSlideFromBottomScope {
    var animTime: Int
    var isInOrOut: MutableState<Boolean?>
    fun show()
    fun hide(callback: () -> Unit)
}

class AnimatedSlideFromBottomScopeImpl : AnimatedSlideFromBottomScope {
    override var animTime: Int = 200
    override var isInOrOut: MutableState<Boolean?> = mutableStateOf(null)
    private var coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    override fun show() {
        if (isInOrOut.value == null) {
            isInOrOut.value = true
        }
    }

    override fun hide(callback: () -> Unit) {
        coroutineScope.launch {
            isInOrOut.value = false
            delay(animTime.toLong())
            callback()
        }
    }
}

@Composable
fun AnimatedSlideFromBottom(content: @Composable AnimatedSlideFromBottomScope.() -> Unit) {
    val animatedScopeImpl = remember { AnimatedSlideFromBottomScopeImpl() }
    LaunchedEffect(Unit) {
        animatedScopeImpl.show()
    }
    AnimatedVisibility(
        visible = animatedScopeImpl.isInOrOut.value == true, enter = slideIn(
            animationSpec = tween(animatedScopeImpl.animTime), initialOffset = {
                IntOffset(
                    x = 0, y = it.height
                )
            }), exit = slideOut(
            animationSpec = tween(animatedScopeImpl.animTime), targetOffset = {
                IntOffset(
                    x = 0, y = it.height
                )
            })
    ) {
        animatedScopeImpl.content()
    }
}