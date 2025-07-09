package com.laomuji1999.compose.core.ui.we.cache

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
    }
}