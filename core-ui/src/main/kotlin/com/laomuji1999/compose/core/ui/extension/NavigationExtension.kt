package com.laomuji1999.compose.core.ui.extension

import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Navigation3 Extension
 * 安全弹出屏幕
 * 一秒钟内只允许栈中弹出一次屏幕
 * 防止用户连续点击返回按钮
 */
private var lastPopBackJob: Job? = null
private val navigationCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

fun MutableList<out NavKey>.nav3PopBackStack() {
    if (lastPopBackJob?.isActive == true) return

    lastPopBackJob = navigationCoroutineScope.launch {
        if (size > 1) {
            removeAt(size - 1)
        }
        delay(1000)
        lastPopBackJob = null
    }
}

/**
 * 用于Graph导航
 * 因为导航必须在主线程运行
 */
fun <T> MutableSharedFlow<T>.emitGraph(data: T) {
    CoroutineScope(SupervisorJob() + Dispatchers.Main).launch {
        emit(data)
    }
}
