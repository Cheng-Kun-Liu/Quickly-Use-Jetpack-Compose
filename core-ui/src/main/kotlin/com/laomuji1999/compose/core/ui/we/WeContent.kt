package com.laomuji1999.compose.core.ui.we

import android.app.Activity
import android.os.Build
import android.view.WindowInsets
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.captionBarPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat
import com.laomuji1999.compose.core.logic.common.Toast
import com.laomuji1999.compose.core.ui.ifCondition
import com.laomuji1999.compose.core.ui.isPreview
import com.laomuji1999.compose.core.ui.we.widget.toast.WeToast
import com.laomuji1999.compose.core.ui.we.widget.toast.WeToastType

/**
 * 设计系统content封装
 * @author laomuji666
 * @since 2025/5/23
 */
@Composable
internal fun WeContent(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val isOldWindowInsetsApi = Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM
    if (!isPreview()) {
        val bottomBarBackground = WeTheme.colorScheme.bottomBarBackground.toArgb()
        val isDarkFont = WeTheme.colorScheme.isDarkFont

        SideEffect {
            val window = (view.context as Activity).window

            //设置底部导航栏颜色
            if (isOldWindowInsetsApi) {
                //API35结束的API,因为限定了版本,可以抑制DEPRECATION
                @Suppress("DEPRECATION")
                window.navigationBarColor = bottomBarBackground
            } else {
                //新Api不允许设置导航栏颜色,改为设置所有的背景色,Compose最外层navigationBarsPadding.
                window.decorView.setOnApplyWindowInsetsListener { view, insets ->
                    view.setBackgroundColor(bottomBarBackground)
                    view.setPadding(
                        0, 0, 0, insets.getInsets(WindowInsets.Type.navigationBars()).bottom
                    )
                    insets
                }
            }

            val windowInsetsController = WindowInsetsControllerCompat(window, view)
            //设置底部导航栏图标是否为深色
            windowInsetsController.isAppearanceLightNavigationBars = isDarkFont
            //设置顶部状态栏文字是否为深色
            windowInsetsController.isAppearanceLightStatusBars = isDarkFont
        }
    }

    //captionBarPadding 是 Desktop和部分非全屏模式出现,官方未提供api定制颜色,添加padding,交由系统控制.
    Box(
        modifier = Modifier
            .captionBarPadding()
            .ifCondition(
                condition = isOldWindowInsetsApi,
                onTrue = {
                    navigationBarsPadding()
                },
            )
    ) {
        content()
    }

    //自定义Toast组件
    var toastText by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        Toast.toastShardFlow.collect {
            toastText = it
        }
    }
    if (toastText.isNotEmpty()) {
        WeToast(
            weToastType = WeToastType.Error,
            message = toastText,
        )
    }
}