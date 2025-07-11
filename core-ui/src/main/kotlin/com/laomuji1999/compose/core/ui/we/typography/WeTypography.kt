package com.laomuji1999.compose.core.ui.we.typography

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

/**
 * @author laomuji666
 * @since 2025/7/9
 */
open class WeTypography(
    val titleEm: TextStyle,
    val title: TextStyle,
    val bodyEm: TextStyle,
    val body: TextStyle,
    val caption: TextStyle,
    val micro: TextStyle
)

val LocalWeTypography: ProvidableCompositionLocal<WeTypography> =
    staticCompositionLocalOf { WeTypography14 }