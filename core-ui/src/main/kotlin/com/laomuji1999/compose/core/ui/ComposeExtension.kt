package com.laomuji1999.compose.core.ui

import androidx.activity.BackEventCompat
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode

/**
 * 当前是不是Preview
 * @author laomuji666
 * @since 2025/5/23
 */
@Composable
fun isPreview() = LocalInspectionMode.current

/**
 * 根据条件返回不同的Modifier
 * @param condition 条件
 * @param onTrue 为true时的Modifier
 * @param onFalse 为false时的Modifier
 * @author laomuji666
 * @since 2025/5/23
 */
@Composable
fun Modifier.ifCondition(
    condition: Boolean,
    onTrue: @Composable Modifier.() -> Modifier,
    onFalse: @Composable Modifier.() -> Modifier = { this }
): Modifier {
    return if (condition) {
        this.onTrue()
    } else {
        this.onFalse()
    }
}

/**
 * 限制点击速度, 防止多次点击
 * @param timeout 点击限速时间
 * @param indication 点击效果
 * @param onClick 点击事件
 * @author laomuji666
 * @since 2025/5/23
 */
@Composable
fun Modifier.clickableDebounce(
    timeout: Long = 300L,
    indication: Indication? = LocalIndication.current,
    onClick: () -> Unit
): Modifier {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    return this.clickable(
        enabled = true,
        indication = indication,
        interactionSource = remember { MutableInteractionSource() },
        onClick = {
            val currentTimeMillis = System.currentTimeMillis()
            if (currentTimeMillis - lastClickTime > timeout) {
                lastClickTime = currentTimeMillis
                onClick()
            }
        })
}

@Composable
fun BackInvokedCallbackProgress(
    onBackHandle: () -> Unit,
    onBackCancel: (() -> Unit)? = null,
    onBackProgressChanged: ((Float?) -> Unit),
) {
    val onBackPressedCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackProgressed(backEvent: BackEventCompat) {
                onBackProgressChanged(backEvent.progress)
            }

            override fun handleOnBackPressed() {
                onBackProgressChanged(null)
                onBackHandle()
            }

            override fun handleOnBackCancelled() {
                onBackProgressChanged(null)
                onBackCancel?.invoke()
            }
        }
    }
    val onBackPressedDispatcher =
        LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher
    DisposableEffect(onBackPressedDispatcher) {
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        onDispose {
            onBackPressedCallback.remove()
        }
    }
}