package com.laomuji1999.compose.core.ui.extension

import android.app.Activity
import android.app.ActivityManager
import android.content.Context

/**
 * 自动清理当前任务栈。
 * 当 Activity 销毁后，检查并移除不再需要的任务项，避免在“最近任务”列表中留下空白卡片。
 * 通常用于配置了独立 taskAffinity 的 Activity。
 * @author laomuji666
 * @since 2025/5/23
 */
fun Activity.finishAndCleanupTask() {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager ?: return
    for (appTask in activityManager.appTasks) {
        val taskInfo = appTask.taskInfo
        // 1. 检查任务栈中是否已经没有 Activity
        val isAllFinish = taskInfo.numActivities == 0
        // 2. 检查任务栈的根 Intent 是否指向当前 Activity 类
        val isSameComponent = componentName.shortClassName == taskInfo.baseIntent.component?.shortClassName

        if (isAllFinish && isSameComponent) {
            appTask.finishAndRemoveTask()
        }
    }
}
