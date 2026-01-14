package com.laomuji1999.compose.feature.video

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.laomuji1999.compose.core.ui.clickableDebounce
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.icons.More
import com.laomuji1999.compose.core.ui.we.icons.WeIcons
import com.laomuji1999.compose.feature.video.player.VideoPlayerView

@Composable
fun VideoScreen(
    videoUri: String,
    isFullScreen: Boolean = true
) {
    val context = LocalContext.current
    LaunchedEffect(isFullScreen) {
        if (isFullScreen) {
            context.hideSystemUi()
        } else {
            context.showSystemUi()
        }
    }
    Box {
        VideoPlayerView(
            modifier = Modifier.fillMaxSize(),
            videoUri = videoUri,
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    vertical = WeTheme.dimens.actionIconSize * 2,
                    horizontal = WeTheme.dimens.actionIconSize,
                )
                .clickableDebounce(
                    indication = null,
                    onClick = {
                        VideoPlayActivity.openVideoOtherApp(context = context, filename = videoUri)
                    }
                )
        ) {
            Image(
                imageVector = WeIcons.More,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                colorFilter = ColorFilter.tint(WeTheme.colorScheme.primaryButton),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(WeTheme.dimens.actionIconSize),
            )
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun Context.hideSystemUi() {
    val activity = this.findActivity() ?: return
    val window = activity.window ?: return
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun Context.showSystemUi() {
    val activity = this.findActivity() ?: return
    val window = activity.window ?: return
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(
        window,
        window.decorView
    ).show(WindowInsetsCompat.Type.systemBars())
}