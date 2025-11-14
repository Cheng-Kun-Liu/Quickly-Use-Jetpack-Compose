package com.laomuji1999.compose.core.ui.we

import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowInsetsControllerCompat

/**
 * 解决弹窗屏幕适配失效的问题
 * 由于Dialog是单独的Window,所以需要额外进行适配.
 *
 * @param onDismissRequest 按下返回键,点击空白区域.
 * @param properties 弹窗配置,默认铺满全屏
 * @param dimProgress 暗色进度,0f-1f
 * @param statusBarLight 状态栏是否是亮色
 * @param statusBarColor 状态栏颜色
 * @param enableTouchBackground 是否允许穿透点击
 * @param content 弹窗实际内容
 */
@Composable
fun WeDialog(
    properties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false,
    ),
    onDismissRequest: () -> Unit = {},
    dimProgress: Float = 1f,
    statusBarLight: Boolean = true,
    statusBarColor: Color = Color.Transparent,
    enableTouchBackground: Boolean = false,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        val dialogWindow = (LocalView.current.parent as DialogWindowProvider).window

        //设置背景暗色进度
        val originDim = remember { dialogWindow.attributes.dimAmount }
        LaunchedEffect(dimProgress) {
            dialogWindow.setDimAmount(originDim * dimProgress)
        }
        //设置状态栏颜色
        LaunchedEffect(statusBarColor) {
            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            @Suppress("DEPRECATION")
            dialogWindow.statusBarColor = statusBarColor.toArgb()
            @Suppress("DEPRECATION")
            dialogWindow.navigationBarColor = statusBarColor.toArgb()
        }
        //设置状态栏是否是亮色
        LaunchedEffect(statusBarLight) {
            WindowInsetsControllerCompat(dialogWindow, dialogWindow.decorView).apply {
                isAppearanceLightStatusBars = statusBarLight
                isAppearanceLightNavigationBars = statusBarLight
            }
        }
        //允许弹窗绘制到系统栏
        LaunchedEffect(Unit) {
            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        //设置是否允许穿透点击
        LaunchedEffect(enableTouchBackground) {
            if (enableTouchBackground) {
                dialogWindow.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                )
            } else {
                dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
            }
        }

        //对弹窗进行屏幕适配
        CompositionLocalProvider(LocalDensity provides getAdapterDensity()) {
            content()
        }
    }
}