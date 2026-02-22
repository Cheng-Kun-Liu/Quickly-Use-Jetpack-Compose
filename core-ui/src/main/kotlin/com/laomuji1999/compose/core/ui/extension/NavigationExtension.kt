package com.laomuji1999.compose.core.ui.extension

import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * 增加一个屏幕
 * 新的屏幕会在添加在当前屏幕后
 */
fun NavHostController.navOptionsPushBack(): NavOptions {
    return navOptions {
        currentDestination?.route?.let {
            popUpTo(it) {
                this.saveState = true
            }
        }
    }
}

/**
 * 增加一个屏幕
 * 从新的屏幕返回时 退出
 */
fun NavHostController.navOptionsRemoveAll(): NavOptions {
    return navOptions {
        popUpTo(0) {
            saveState = false
            graph
            inclusive = true
        }
    }
}

/**
 * 一秒钟内只允许栈中弹出一次屏幕
 * 防止用户连续点击返回按钮
 */
private var lastPopBackJob: Job? = null
private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
fun NavHostController.safePopBackStack() {
    if (lastPopBackJob?.isActive == true) return

    lastPopBackJob = coroutineScope.launch {
        popBackStack()
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