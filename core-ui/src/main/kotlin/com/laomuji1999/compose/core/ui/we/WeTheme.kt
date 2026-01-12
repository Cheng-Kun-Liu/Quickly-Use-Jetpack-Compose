package com.laomuji1999.compose.core.ui.we

import android.content.res.Configuration
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.laomuji1999.compose.core.ui.WeIndication
import com.laomuji1999.compose.core.ui.we.colorscheme.LocalWeColorScheme
import com.laomuji1999.compose.core.ui.we.colorscheme.WeColorScheme
import com.laomuji1999.compose.core.ui.we.typography.LocalWeTypography
import com.laomuji1999.compose.core.ui.we.typography.WeTypography


/**
 * 设计系统入口
 * 把值设为默认值
 *
 * @param weDimens 尺寸
 * @param weColorScheme 颜色
 * @param weTypography 字体
 * @param content 内容
 *
 * @author laomuji666
 * @since 2025/5/23
 */
@Composable
fun WeTheme(
    weDimens: WeDimens,
    weColorScheme: WeColorScheme,
    weTypography: WeTypography,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalDensity provides getAdapterDensity(),
        LocalIndication provides remember(weColorScheme) {
            WeIndication(weColorScheme.pressed)
        },
        LocalWeColorScheme provides weColorScheme,
        LocalWeDimens provides weDimens,
        LocalWeTypography provides weTypography,
        LocalOverscrollFactory provides null
    ) {
        WeContent(content = content)
    }
}

/**
 * 参考MaterialTheme,获取当前正在使用的设计系统的主题值.
 * @author laomuji666
 * @since 2025/5/23
 */
object WeTheme {
    val colorScheme: WeColorScheme
        @Composable @ReadOnlyComposable get() = LocalWeColorScheme.current

    val dimens: WeDimens
        @Composable @ReadOnlyComposable get() = LocalWeDimens.current

    val typography: WeTypography
        @Composable @ReadOnlyComposable get() = LocalWeTypography.current
}

/**
 * 获取屏幕适配
 */
@Composable
internal fun getAdapterDensity(designWidth: Float = 375f): Density {
    val orientation = LocalConfiguration.current.orientation
    if (orientation != Configuration.ORIENTATION_PORTRAIT) {
        return LocalDensity.current
    } else {
        val resources = LocalContext.current.resources
        val displayMetrics = resources.displayMetrics
        val targetDensity = displayMetrics.widthPixels / designWidth
        //字体缩放固定为1f,不受系统调整字体大小的影响.
        return Density(density = targetDensity, fontScale = 1f)
    }
}