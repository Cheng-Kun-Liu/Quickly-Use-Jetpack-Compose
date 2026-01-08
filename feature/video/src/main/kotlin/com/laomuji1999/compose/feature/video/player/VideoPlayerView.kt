package com.laomuji1999.compose.feature.video.player

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView

/**
 * @param modifier Modifier
 * @param url 视频地址
 * @param playResizeMode 播放模式 [androidx.media3.ui.AspectRatioFrameLayout.ResizeMode]
 * @param isRepeat 是否循环播放
 * @param showController 是否显示控制器
 * @param isPause 是否暂停
 */
@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerView(
    modifier: Modifier = Modifier,
    url: String,
    playResizeMode: Int? = null,
    isRepeat: Boolean = true,
    showController: Boolean = true,
    isPause: Boolean = false,
) {
    val context = LocalContext.current
    VideoPlayerViewCache.init(context)
    var player by remember { mutableStateOf<ExoPlayer?>(null) }
    var currentUrl by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(url) {
        if (player == null) {
            player = ExoPlayer.Builder(
                context, DefaultRenderersFactory(context).setEnableDecoderFallback(true)
            ).setMediaSourceFactory(
                DefaultMediaSourceFactory(VideoPlayerViewCache.createMediaSourceFactory())
            ).build()
        }

        if (currentUrl != url) {
            currentUrl = url
            val mediaItem = MediaItem.fromUri(url.toUri())
            player?.apply {
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
            }
        }
    }

    LaunchedEffect(player, isRepeat) {
        player?.repeatMode = if (isRepeat) Player.REPEAT_MODE_ONE
        else Player.REPEAT_MODE_OFF
        player?.playWhenReady = true
    }

    LaunchedEffect(isPause) {
        if (isPause) {
            player?.playWhenReady = false
            player?.pause()
        } else {
            player?.playWhenReady = true
            player?.play()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player?.release()
            player = null
        }
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            PlayerView(context).apply {
                this.player = player
                useController = showController
                playResizeMode?.let { resizeMode = it }
            }
        },
        update = { playerView ->
            playerView.apply {
                this.player = player
                useController = showController
                playResizeMode?.let { resizeMode = it }
            }
        },
    )
}