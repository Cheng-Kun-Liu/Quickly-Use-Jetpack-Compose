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

    companion object {
        private val _currentWeTypography = MutableStateFlow<WeTypography>(
            WeTypography13
        )

        val currentWeTypography = _currentWeTypography.asStateFlow()

        fun setWeTypography(weTypography: WeTypography) {
            _currentWeTypography.update {
                weTypography
            }
        }

        fun setWeTypography(clsName: String?) {
            val weTypography = when (clsName) {
                WeTypography13::class.java.name -> WeTypography13
                WeTypography14::class.java.name -> WeTypography14
                WeTypography15::class.java.name -> WeTypography15
                WeTypography16::class.java.name -> WeTypography16
                WeTypography17::class.java.name -> WeTypography17
                else -> WeTypography14
            }
            setWeTypography(weTypography)
        }
    }
}