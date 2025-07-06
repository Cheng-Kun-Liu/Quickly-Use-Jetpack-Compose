package com.laomuji1999.compose.core.ui.we.colorscheme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

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
    brandColor: Color = WeColors.Color_07C160,
    onBrandColor: Color = WeColors.Color_FFFFFFFF,
): WeColorScheme = object : WeColorScheme(
    isDarkFont = !isDarkTheme,

    background = background,
    cursorColor = brandColor,

    fontColorHeavy = onBackground,
    fontColorLight = onSurfaceVariant,
    fontColorError = error,
    fontColorPrimary = brandColor,

    primaryButton = brandColor,
    onPrimaryButton = onBrandColor,

    secondaryButton = surfaceVariant,
    onSecondaryButton = brandColor,

    disableButton = surfaceVariant,
    onDisableButton = onSurfaceVariant.copy(alpha = 0.3f),

    wrongButton = surfaceVariant,
    onWrongButton = error,

    outline = outline,
    pressed = surfaceTint.copy(alpha = if (isDarkTheme) 0.05f else 0.08f),

    rowBackground = surfaceVariant,

    switchThumbColor = onPrimary,
    switchSelectBackground = brandColor,
    switchUnSelectBackground = outlineVariant ?: outline,

    bottomBarSelect = brandColor,
    bottomBarUnSelect = onSurfaceVariant,
    bottomBarBackground = surface,

    toastBackgroundColor = surfaceVariant,
    onToastBackgroundColor = onSurface,

    chatInputBackground = surfaceVariant,
    chatMessageBackgroundSend = primaryContainer,
    chatMessageBackgroundReceive = surfaceVariant,
    chatMessageTextSend = onPrimaryContainer,
    chatMessageTextReceive = onSurface,

    categoryTextColor = onPrimary,
    categoryBackground = brandColor
) {}