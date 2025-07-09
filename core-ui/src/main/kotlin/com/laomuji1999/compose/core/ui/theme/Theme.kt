package com.laomuji1999.compose.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.laomuji1999.compose.core.ui.we.WeDimensDefault
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.cache.WeCacheColorScheme
import com.laomuji1999.compose.core.ui.we.cache.WeCacheTypography
import com.laomuji1999.compose.core.ui.we.colorscheme.WeColorSchemeBlue
import com.laomuji1999.compose.core.ui.we.colorscheme.WeColorSchemeDark
import com.laomuji1999.compose.core.ui.we.colorscheme.WeColorSchemeLight
import com.laomuji1999.compose.core.ui.we.colorscheme.toWeColorScheme
import com.laomuji1999.compose.core.ui.we.typography.WeTypography13
import com.laomuji1999.compose.core.ui.we.typography.WeTypography14
import com.laomuji1999.compose.core.ui.we.typography.WeTypography15

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
    val hasDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val weColorScheme = when (weCacheColorScheme) {
        WeCacheColorScheme.FlowSystem -> if (isSystemInDarkTheme()) WeColorSchemeDark else WeColorSchemeLight
        WeCacheColorScheme.Dynamic -> if (hasDynamicColor) {
            if (isSystemInDarkTheme()) {
                dynamicDarkColorScheme(LocalContext.current).toWeColorScheme(isDarkTheme = true)
            } else {
                dynamicLightColorScheme(LocalContext.current).toWeColorScheme(isDarkTheme = false)
            }
        } else {
            if (isSystemInDarkTheme()) {
                WeColorSchemeDark
            } else {
                WeColorSchemeLight
            }
        }

        WeCacheColorScheme.Light -> WeColorSchemeLight
        WeCacheColorScheme.Dark -> WeColorSchemeDark
        WeCacheColorScheme.Blue -> WeColorSchemeBlue
    }

    val weCacheTypography by WeCacheTypography.currentWeCacheTypography.collectAsStateWithLifecycle()
    val weTypography = when (weCacheTypography) {
        WeCacheTypography.Size13 -> WeTypography13
        WeCacheTypography.Size14 -> WeTypography14
        WeCacheTypography.Size15 -> WeTypography15
    }

    WeTheme(
        weColorScheme = weColorScheme,
        weDimens = WeDimensDefault,
        weTypography = weTypography,
        content = content,
    )
}