package com.laomuji1999.compose.feature.video.player

import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.RetainedEffect
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.compose.ContentFrame
import com.laomuji1999.compose.core.ui.extension.clickableDebounce
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.icons.Loading
import com.laomuji1999.compose.core.ui.we.icons.Pause
import com.laomuji1999.compose.core.ui.we.icons.Play
import com.laomuji1999.compose.core.ui.we.icons.WeIcons
import com.laomuji1999.compose.core.ui.we.widget.slider.WeSlider
import kotlinx.coroutines.delay

/**
 * @param modifier Modifier
 * @param videoUri 视频地址
 */
@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerView(
    modifier: Modifier = Modifier,
    videoUri: String,
    scaleModels: List<Pair<ContentScale, String>> = listOf(
        Pair(ContentScale.Fit, "Fit"),
        Pair(ContentScale.None, "None"),
        Pair(ContentScale.Crop, "Crop"),
        Pair(ContentScale.FillBounds, "FillBounds"),
        Pair(ContentScale.FillWidth, "FillWidth"),
        Pair(ContentScale.FillHeight, "FillHeight"),
        Pair(ContentScale.Inside, "Inside"),
    )
) {
    //是否正在播放
    var isPlaying by retain {
        mutableStateOf(false)
    }
    //是否正在缓冲
    var isBuffering by retain {
        mutableStateOf(false)
    }
    //总播放时长
    var duration by retain {
        mutableLongStateOf(0L)
    }
    //当前播放时长
    var currentPosition by retain {
        mutableLongStateOf(0L)
    }
    //缩放模式
    var scaleModeIndex by retain {
        mutableIntStateOf(0)
    }
    //是否显示控制栏
    var showControlBar by retain {
        mutableStateOf(true)
    }
    //上次操作时间
    var lastActionTime by retain {
        mutableLongStateOf(0L)
    }

    //创建播放器
    val context = LocalContext.current.applicationContext
    val player = retain {
        VideoPlayerViewCache.init(context)
        ExoPlayer.Builder(
            context,
            DefaultRenderersFactory(context).setEnableDecoderFallback(true)
        ).setMediaSourceFactory(
            DefaultMediaSourceFactory(VideoPlayerViewCache.createMediaSourceFactory())
        ).build().apply {
            repeatMode = Player.REPEAT_MODE_ONE
            playWhenReady = true
        }
    }
    //添加播放状态变更事件
    RetainedEffect(player) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                super.onIsPlayingChanged(playing)
                isPlaying = playing
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                isBuffering = playbackState == Player.STATE_BUFFERING
                if (playbackState == Player.STATE_READY) {
                    duration = player.duration.coerceAtLeast(0)
                }
            }
        }
        player.addListener(listener)

        onRetire {
            player.removeListener(listener)
            player.release()
        }
    }
    //防止重复设置url
    var currentUrl by retain { mutableStateOf<String?>(null) }
    LaunchedEffect(videoUri) {
        if (currentUrl != videoUri) {
            currentUrl = videoUri
            val mediaItem = MediaItem.fromUri(videoUri.toUri())
            player.apply {
                seekTo(0)
                setMediaItem(mediaItem)
                prepare()
            }
        }
    }
    //设置当前播放时间
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            currentPosition = player.currentPosition.coerceAtLeast(0)
            delay(20)
        }
    }
    //隐藏控制栏
    LaunchedEffect(showControlBar, lastActionTime) {
        delay(3000L)
        showControlBar = false
    }

    //ui显示
    Box(modifier = modifier) {
        ContentFrame(
            player = player,
            modifier = Modifier
                .fillMaxSize()
                .clickableDebounce(indication = null) {
                    showControlBar = !showControlBar
                },
            contentScale = scaleModels[scaleModeIndex].first,
        )
        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = showControlBar,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                VideoPlayerControlView(
                    isBuffering = isBuffering,
                    currentSecond = (currentPosition / 1000L).toInt(),
                    totalSecond = (duration / 1000L).toInt(),
                    isPlaying = isPlaying,
                    onPlayingChange = {
                        if (it) {
                            player.play()
                        } else {
                            player.pause()
                        }
                        lastActionTime = System.currentTimeMillis()
                    },
                    onSeekChange = {
                        player.seekTo(it * 1000L)
                        lastActionTime = System.currentTimeMillis()
                    },
                    scaleModels = scaleModels,
                    scaleModeIndex = scaleModeIndex,
                    onScaleModelChange = {
                        scaleModeIndex = it
                        lastActionTime = System.currentTimeMillis()
                    },
                )
            }
        }
    }
}

@Composable
private fun BoxScope.VideoPlayerControlView(
    isBuffering: Boolean,
    currentSecond: Int,
    totalSecond: Int,
    isPlaying: Boolean,
    onPlayingChange: (Boolean) -> Unit,
    onSeekChange: (Int) -> Unit,
    scaleModels: List<Pair<ContentScale, String>>,
    scaleModeIndex: Int,
    onScaleModelChange: (Int) -> Unit,
) {
    //显示加载中
    if (isBuffering) {
        val infiniteTransition =
            rememberInfiniteTransition(label = "WeToastType.Loading")
        val rotateDegree by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "VideoPlayer.Loading"
        )
        Image(
            imageVector = WeIcons.Loading,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .align(Alignment.Center)
                .width(WeTheme.dimens.toastIconSize)
                .rotate(rotateDegree)
        )
    }
    //显示底部进度条
    Column(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(WeTheme.dimens.rowPaddingHorizontal),
        verticalArrangement = Arrangement.spacedBy(WeTheme.dimens.rowInnerPaddingHorizontal)
    ) {
        WeSlider(
            value = currentSecond,
            minValue = 0,
            maxValue = totalSecond,
            step = 1,
            onValueChanged = {
                onSeekChange(it)
            },
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${currentSecond}/${totalSecond}",
                style = WeTheme.typography.body,
                color = WeTheme.colorScheme.fontColorPrimary,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier
                    .clickableDebounce(
                        indication = null,
                        onClick = {
                            onScaleModelChange((scaleModeIndex + 1) % scaleModels.size)
                        }
                    ),
                text = scaleModels[scaleModeIndex].second,
                style = WeTheme.typography.body,
                color = WeTheme.colorScheme.fontColorPrimary,
            )
        }
    }
    //显示暂停/播放按钮
    Image(
        imageVector = if (isPlaying) WeIcons.Pause else WeIcons.Play,
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .align(Alignment.Center)
            .width(WeTheme.dimens.toastIconSize)
            .clickableDebounce(indication = null) {
                onPlayingChange(!isPlaying)
            }
    )
}