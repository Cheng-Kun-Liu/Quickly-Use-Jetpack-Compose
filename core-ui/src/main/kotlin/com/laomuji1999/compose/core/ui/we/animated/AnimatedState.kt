package com.laomuji1999.compose.core.ui.we.animated

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnimatedState(
    initShowAnimate: Boolean,
    val startDuration: Int,
    val endDuration: Int,
) {
    private val _isShowAnimate = MutableStateFlow(initShowAnimate)
    val isShowAnimate = _isShowAnimate.asStateFlow()

    fun show(
        coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        callback: suspend () -> Unit = {}
    ) {
        _isShowAnimate.value = true
        coroutineScope.launch {
            delay(startDuration.toLong())
            callback()
        }
    }

    fun hide(
        coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        callback: suspend () -> Unit
    ) {
        _isShowAnimate.value = false
        coroutineScope.launch {
            delay(endDuration.toLong())
            callback()
        }
    }

    companion object {
        val Saver: Saver<AnimatedState, *> =
            listSaver(
                save = {
                    listOf(
                        it.isShowAnimate.value,
                        it.startDuration,
                        it.endDuration,
                    )
                },
                restore = {
                    AnimatedState(
                        it[0] as Boolean,
                        it[1] as Int,
                        it[2] as Int,
                    )
                }
            )

        @Composable
        fun rememberAnimatedState(
            initShowAnimate: () -> Boolean = { false },
            startDuration: Int = 300,
            endDuration: Int = startDuration,
        ) = rememberSaveable(saver = Saver) {
            AnimatedState(
                initShowAnimate = initShowAnimate(),
                startDuration = startDuration,
                endDuration = endDuration,
            )
        }
    }
}