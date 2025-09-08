package com.laomuji1999.compose.core.ui.we

import android.view.WindowManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowInsetsControllerCompat
import com.laomuji1999.compose.core.ui.clickableDebounce

/**
 * 解决弹窗屏幕适配失效的问题
 * 由于Dialog是单独的Window,所以需要额外进行适配.
 */
@Composable
fun WeDialog(
    onDismissRequest: () -> Unit = {},
    properties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false,
        decorFitsSystemWindows = false,
    ),
    dimProgress: Float = 1f,
    lightStatusBars: Boolean = true,
    statusBarColor: Color = Color.Transparent,
    content: @Composable BoxScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        val dialogWindow = (LocalView.current.parent as DialogWindowProvider).window
        val originDim = remember { dialogWindow.attributes.dimAmount }
        LaunchedEffect(dimProgress) {
            dialogWindow.setDimAmount(originDim * dimProgress)
        }

        LaunchedEffect(statusBarColor) {
            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            @Suppress("DEPRECATION")
            dialogWindow.statusBarColor = statusBarColor.toArgb()
            @Suppress("DEPRECATION")
            dialogWindow.navigationBarColor = statusBarColor.toArgb()
        }

        LaunchedEffect(lightStatusBars) {
            WindowInsetsControllerCompat(dialogWindow, dialogWindow.decorView).apply {
                isAppearanceLightStatusBars = lightStatusBars
                isAppearanceLightNavigationBars = lightStatusBars
            }
        }
        CompositionLocalProvider(LocalDensity provides getAdapterDensity()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .clickableDebounce(indication = null, onClick = onDismissRequest)
            ) {
                content()
            }
        }
    }
}