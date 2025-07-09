package com.laomuji1999.compose.core.ui.we.cache

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * 字体大小
 * @author laomuji666
 * @since 2025/7/9
 */
sealed class WeCacheTypography {
    data object Size13 : WeCacheTypography()
    data object Size14 : WeCacheTypography()
    data object Size15 : WeCacheTypography()


    companion object {
        private val _currentWeCacheTypography = MutableStateFlow<WeCacheTypography>(
            Size14
        )

        val currentWeCacheTypography = _currentWeCacheTypography.asStateFlow()

        fun setWeCacheTypography(weCacheColorScheme: WeCacheTypography) {
            _currentWeCacheTypography.update {
                weCacheColorScheme
            }
        }

        fun setWeCacheTypography(clsName: String?) {
            val weCacheTypography = when (clsName) {
                Size13::class.java.name -> Size13
                Size14::class.java.name -> Size14
                Size15::class.java.name -> Size15
                else -> Size14
            }
            setWeCacheTypography(weCacheTypography)
        }
    }
}