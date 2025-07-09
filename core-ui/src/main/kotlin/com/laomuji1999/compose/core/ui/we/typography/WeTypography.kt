package com.laomuji1999.compose.core.ui.we.typography

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

/**
 * @param titleEm 弹窗标题
 * @param title 主要字体大小.
 * @param body 次要字体大小.
 * @param caption 不需要引起注意的字体大小,比如时间.
 * @param micro 最小字体.
 * @author laomuji666
 * @since 2025/7/9
 */
open class WeTypography(
    val titleEm: TextStyle,
    val title: TextStyle,
    val body: TextStyle,
    val caption: TextStyle,
    val micro: TextStyle
)

internal val LocalWeTypography: ProvidableCompositionLocal<WeTypography> =
    staticCompositionLocalOf { WeTypography14 }