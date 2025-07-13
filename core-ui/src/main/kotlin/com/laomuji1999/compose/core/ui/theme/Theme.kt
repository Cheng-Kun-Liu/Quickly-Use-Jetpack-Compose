package com.laomuji1999.compose.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.laomuji1999.compose.core.ui.we.WeDimensDefault
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.cache.WeCacheColorScheme
import com.laomuji1999.compose.core.ui.we.cache.WeCacheColorScheme.Companion.toWeColorScheme
import com.laomuji1999.compose.core.ui.we.cache.WeCacheTypography

/**
 * 设计系统快速入口,不接受自定义主题.
 * 如果需要自定义,需要使用[WeTheme].
 * @author laomuji666
 * @since 2025/5/23
 */
@Composable
fun QuicklyTheme(
    content: @Composable () -> Unit
) {
    val weCacheColorScheme by WeCacheColorScheme.currentWeThemeColorType.collectAsStateWithLifecycle()
    val weTypography by WeCacheTypography.currentWeTypography.collectAsStateWithLifecycle()
    WeTheme(
        weDimens = WeDimensDefault,
        weColorScheme = weCacheColorScheme.toWeColorScheme(),
        weTypography = weTypography,
        content = content,
    )
}