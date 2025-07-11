package com.laomuji1999.compose.core.ui.we.cache

import com.laomuji1999.compose.core.ui.we.typography.WeTypography
import com.laomuji1999.compose.core.ui.we.typography.WeTypography13
import com.laomuji1999.compose.core.ui.we.typography.WeTypography14
import com.laomuji1999.compose.core.ui.we.typography.WeTypography15
import com.laomuji1999.compose.core.ui.we.typography.WeTypography16
import com.laomuji1999.compose.core.ui.we.typography.WeTypography17
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
    data object Size16 : WeCacheTypography()
    data object Size17 : WeCacheTypography()


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
                Size16::class.java.name -> Size16
                Size17::class.java.name -> Size17
                else -> Size14
            }
            setWeCacheTypography(weCacheTypography)
        }


        fun WeCacheTypography.toWeTypography(): WeTypography {
            return when (this) {
                Size13 -> WeTypography13
                Size14 -> WeTypography14
                Size15 -> WeTypography15
                Size16 -> WeTypography16
                Size17 -> WeTypography17
            }
        }

        fun WeTypography.toWeCacheTypography(): WeCacheTypography {
            return when (this) {
                WeTypography13 -> Size13
                WeTypography14 -> Size14
                WeTypography15 -> Size15
                WeTypography16 -> Size16
                WeTypography17 -> Size17
                else -> Size14
            }
        }
    }
}