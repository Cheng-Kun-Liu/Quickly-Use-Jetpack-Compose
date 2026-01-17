package com.laomuji1999.compose.core.ui.we.colorscheme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.laomuji1999.compose.core.logic.common.Log

internal val LocalWeColorScheme: ProvidableCompositionLocal<WeColorScheme> =
    staticCompositionLocalOf { WeColorSchemeLight }


/**
 * 颜色设计
 * @author laomuji666
 * @since 2025/5/23
 */
open class WeColorScheme(
    //顶部状态栏文字是否是深色
    val isDarkFont: Boolean,

    //背景色
    val background: Color,
    //光标色
    val cursorColor: Color,

    //重字体色
    val fontColorHeavy: Color,
    //轻字体色
    val fontColorLight: Color,
    //异常字体色
    val fontColorError: Color,
    //主字体色
    val fontColorPrimary: Color,

    //主操作按钮
    val primaryButton: Color,
    val onPrimaryButton: Color,
    //弱化按钮
    val secondaryButton: Color,
    val onSecondaryButton: Color,
    //失效按钮
    val disableButton: Color,
    val onDisableButton: Color,
    //警告按钮
    val wrongButton: Color,
    val onWrongButton: Color,

    //分割线色
    val outline: Color,
    //可点击的触摸色
    val pressed: Color,

    //列表组件背景色
    val rowBackground: Color,

    //开关组件颜色
    val switchThumbColor: Color,
    val switchSelectBackground: Color,
    val switchUnSelectBackground: Color,

    //底部导航组件颜色
    val bottomBarSelect: Color,
    val bottomBarUnSelect: Color,
    val bottomBarBackground: Color,

    //Toast组件颜色
    val toastBackgroundColor: Color,
    val onToastBackgroundColor: Color,

    //聊天页面,特定颜色
    val chatInputBackground: Color,
    val chatMessageBackgroundSend: Color,
    val chatMessageBackgroundReceive: Color,
    val chatMessageTextSend: Color,
    val chatMessageTextReceive: Color,

    //联系人页面,特定颜色
    val categoryTextColor: Color,
    val categoryBackground: Color,
)

fun ColorScheme.toWeColorScheme(
    isDarkTheme: Boolean,
): WeColorScheme = WeColorScheme(
    isDarkFont = !isDarkTheme,

    background = if (isDarkTheme) background else Color(0xFFF5F5F5),
    cursorColor = primary,

    fontColorHeavy = if (isDarkTheme) Color.White.copy(alpha = 0.9f) else Color.Black.copy(alpha = 0.9f),
    fontColorLight = if (isDarkTheme) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.5f),
    fontColorError = error,
    fontColorPrimary = primary,

    primaryButton = primary,
    onPrimaryButton = onPrimary,

    secondaryButton = if (isDarkTheme) Color.White.copy(alpha = 0.15f) else Color.Black.copy(
        alpha = 0.05f
    ),
    onSecondaryButton = primary,

    disableButton = surfaceVariant.copy(alpha = 0.5f),
    onDisableButton = outline.copy(alpha = 0.5f),

    wrongButton = error.copy(alpha = 0.2f),
    onWrongButton = error,

    outline = outline.copy(alpha = 0.5f),
    pressed = primary.copy(alpha = 0.1f),

    bottomBarBackground = surface,
    bottomBarSelect = primary,
    bottomBarUnSelect = if (isDarkTheme) Color.White.copy(alpha = 0.5f) else Color.Black.copy(
        alpha = 0.5f
    ),

    toastBackgroundColor = surface.copy(alpha = 0.9f),
    onToastBackgroundColor = onSurface,

    switchThumbColor = surface,
    switchSelectBackground = primary,
    switchUnSelectBackground = outline.copy(alpha = 0.5f),

    rowBackground = surfaceVariant,
    chatInputBackground = surfaceVariant,

    chatMessageBackgroundSend = primary,
    chatMessageBackgroundReceive = surfaceVariant,
    chatMessageTextSend = onPrimary,
    chatMessageTextReceive = onSurface,

    categoryTextColor = onPrimary,
    categoryBackground = primary,
)
