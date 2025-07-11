package com.laomuji1999.compose.core.ui.we.cache

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.laomuji1999.compose.core.ui.we.colorscheme.WeColorScheme
import com.laomuji1999.compose.core.ui.we.colorscheme.WeColorSchemeBlue
import com.laomuji1999.compose.core.ui.we.colorscheme.WeColorSchemeDark
import com.laomuji1999.compose.core.ui.we.colorscheme.WeColorSchemeLight
import com.laomuji1999.compose.core.ui.we.colorscheme.toWeColorScheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * 颜色风格类型
 * @author laomuji666
 * @since 2025/5/23
 */
sealed class WeCacheColorScheme {
    data object FlowSystem : WeCacheColorScheme()
    data object Dynamic : WeCacheColorScheme()
    data object Light : WeCacheColorScheme()
    data object Dark : WeCacheColorScheme()
    data object Blue : WeCacheColorScheme()


    companion object {
        private val _currentWeCacheColorScheme = MutableStateFlow<WeCacheColorScheme>(
            FlowSystem
        )

        val currentWeThemeColorType = _currentWeCacheColorScheme.asStateFlow()

        fun setWeThemeColorType(weCacheColorScheme: WeCacheColorScheme) {
            _currentWeCacheColorScheme.update {
                weCacheColorScheme
            }
        }

        fun setWeThemeColorType(clsName: String?) {
            val weThemeColorType = when (clsName) {
                FlowSystem::class.java.name -> FlowSystem
                Dynamic::class.java.name -> Dynamic
                Light::class.java.name -> Light
                Dark::class.java.name -> Dark
                Blue::class.java.name -> Blue
                else -> FlowSystem
            }
            setWeThemeColorType(weThemeColorType)
        }

        @Composable
        fun WeCacheColorScheme.toWeColorScheme(): WeColorScheme {
            val hasDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            return when (this) {
                FlowSystem -> if (isSystemInDarkTheme()) WeColorSchemeDark else WeColorSchemeLight
                Dynamic -> if (hasDynamicColor) {
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

                Light -> WeColorSchemeLight
                Dark -> WeColorSchemeDark
                Blue -> WeColorSchemeBlue
            }
        }
    }
}