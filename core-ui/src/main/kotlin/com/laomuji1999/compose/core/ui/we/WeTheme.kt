package com.laomuji1999.compose.core.ui.we

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.util.TypedValue
import android.view.WindowInsets
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.captionBarPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowInsetsControllerCompat
import com.laomuji1999.compose.core.ui.WeIndication
import com.laomuji1999.compose.core.ui.clickableDebounce
import com.laomuji1999.compose.core.ui.ifCondition
import com.laomuji1999.compose.core.ui.isPreview
import com.laomuji1999.compose.core.ui.we.colorscheme.LocalWeColorScheme
import com.laomuji1999.compose.core.ui.we.colorscheme.WeColorScheme


/**
 * 设计系统入口
 * 把值设为默认值
 *
 * @param weColorScheme 颜色
 * @param weDimens 尺寸
 * @param weTypography 字体
 * @param content 内容
 *
 * @author laomuji666
 * @since 2025/5/23
 */
@Composable
fun WeTheme(
    weColorScheme: WeColorScheme,
    weDimens: WeDimens,
    weTypography: WeTypography,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalDensity provides getAdapterDensity(),
        LocalIndication provides remember(weColorScheme) {
            WeIndication(weColorScheme.pressed)
        },
        LocalWeColorScheme provides weColorScheme,
        LocalWeDimens provides weDimens,
        LocalWeTypography provides weTypography,
    ) {
        WeBaseContent(content = content)
    }
}

/**
 * 设计系统content封装
 * @author laomuji666
 * @since 2025/5/23
 */
@Composable
private fun WeBaseContent(
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

    Box(
        modifier = Modifier
            .captionBarPadding() //Desktop和部分非全屏模式出现,官方未提供api定制颜色,添加padding,交由系统控制.
            .ifCondition(
                condition = isOldWindowInsetsApi,
                onTrue = {
                    navigationBarsPadding()
                },
            )
    ) {
        content()
    }
}

/**
 * 参考MaterialTheme,获取当前正在使用的设计系统的主题值.
 * @author laomuji666
 * @since 2025/5/23
 */
object WeTheme {
    val colorScheme: WeColorScheme
        @Composable @ReadOnlyComposable get() = LocalWeColorScheme.current

    val dimens: WeDimens
        @Composable @ReadOnlyComposable get() = LocalWeDimens.current

    val typography: WeTypography
        @Composable @ReadOnlyComposable get() = LocalWeTypography.current
}


@Composable
internal fun getAdapterDensity(designWidth: Float = 375f): Density {
    val orientation = LocalConfiguration.current.orientation
    if (orientation != Configuration.ORIENTATION_PORTRAIT) {
        return LocalDensity.current
    } else {
        val context = LocalContext.current
        val resources = context.resources
        val displayMetrics = resources.displayMetrics
        val targetDensity = displayMetrics.widthPixels / designWidth
        val systemScaledPxPerSp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 1f, displayMetrics
        )
        val fontScale = systemScaledPxPerSp / targetDensity
        return Density(density = targetDensity, fontScale = fontScale)
    }
}

/**
 * 解决弹窗屏幕适配失效的问题
 */
@Composable
fun WeDialog(
    onDismissRequest: () -> Unit = {},
    properties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false,
        decorFitsSystemWindows = false,
    ),
    dimProgress: Float = 1f,
    lightStatusBars: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        val dialogWindow = (LocalView.current.parent as DialogWindowProvider).window
        val originDim = remember { dialogWindow.attributes.dimAmount }
        LaunchedEffect(dimProgress) {
            dialogWindow.setDimAmount(originDim * dimProgress)
        }
        LaunchedEffect(lightStatusBars) {
            WindowInsetsControllerCompat(dialogWindow, dialogWindow.decorView).apply {
                isAppearanceLightStatusBars = lightStatusBars
                isAppearanceLightNavigationBars = lightStatusBars
            }
        }
        CompositionLocalProvider(LocalDensity provides getAdapterDensity()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .clickableDebounce(indication = null, onClick = onDismissRequest)
            ) {
                content()
            }
        }
    }
}