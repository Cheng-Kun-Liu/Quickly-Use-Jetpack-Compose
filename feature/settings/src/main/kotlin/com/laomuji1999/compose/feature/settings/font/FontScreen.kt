package com.laomuji1999.compose.feature.settings.font

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.typography.LocalWeTypography
import com.laomuji1999.compose.core.ui.we.widget.button.WeButton
import com.laomuji1999.compose.core.ui.we.widget.button.WeButtonColor
import com.laomuji1999.compose.core.ui.we.widget.button.WeButtonType
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutline
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutlineType
import com.laomuji1999.compose.core.ui.we.widget.scaffold.WeScaffold
import com.laomuji1999.compose.core.ui.we.widget.slider.WeSlider
import com.laomuji1999.compose.core.ui.we.widget.topbar.WeTopBar
import com.laomuji1999.compose.feature.settings.FontScreenRoute
import com.laomuji1999.compose.res.R
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun FontScreen(
    viewModel: FontScreenViewModel = hiltViewModel(),
    navigateToGraph: (FontScreenRoute.Graph) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.graph.collect {
            navigateToGraph(it)
        }
    }

    FontScreenUi(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        onAction = viewModel::onAction,
    )
}

@Composable
fun FontScreenUi(
    uiState: FontScreenUiState,
    onAction: (FontScreenAction) -> Unit,
) {
    if (uiState.allWeTypography.isEmpty()) {
        return
    }
    val weTypography = uiState.currentWeTypography ?: LocalWeTypography.current
    WeScaffold(
        topBar = {
            WeTopBar(title = stringResource(id = R.string.string_font_screen_title), onBackClick = {
                onAction(FontScreenAction.OnClickBack)
            }, actions = {
                WeButton(
                    text = stringResource(id = R.string.string_font_screen_confirm),
                    weButtonType = WeButtonType.Warp,
                    weButtonColor = WeButtonColor.Primary,
                    onClick = {
                        onAction(FontScreenAction.OnConfirm)
                    })
            })
            WeOutline(weOutlineType = WeOutlineType.Full)
        }) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            CompositionLocalProvider(
                LocalWeTypography provides weTypography
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = WeTheme.dimens.chatPadding),
                ) {
                    Spacer(modifier = Modifier.height(WeTheme.dimens.chatPadding * 2))
                    ChatMessageText(
                        text = "${stringResource(R.string.string_font_screen_title)} ",
                        isSend = true,
                    )
                    Spacer(modifier = Modifier.height(WeTheme.dimens.chatPadding * 2))
                    ChatMessageText(
                        text = "${stringResource(R.string.string_font_screen_title)} ".repeat(3),
                        isSend = false,
                    )
                    Spacer(modifier = Modifier.height(WeTheme.dimens.chatPadding * 2))
                    ChatMessageText(
                        text = "${stringResource(R.string.string_font_screen_title)} ".repeat(10),
                        isSend = false,
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .background(WeTheme.colorScheme.rowBackground)
                .fillMaxWidth()
                .padding(WeTheme.dimens.chatPadding * 2)
        ) {

            WeSlider(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.currentIndex,
                minValue = 0,
                maxValue = uiState.allWeTypography.size - 1,
                step = 1,
                onValueChanged = {
                    onAction(FontScreenAction.OnChangeTypography(it))
                },
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(WeTheme.dimens.chatPadding / 2)
            )


            var paddingStart by remember { mutableIntStateOf(0) }
            var paddingEnd by remember { mutableIntStateOf(0) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
            ) {
                CompositionLocalProvider(
                    LocalWeTypography provides uiState.allWeTypography.first()
                ) {
                    Text(
                        text = stringResource(R.string.string_font_screen_example),
                        style = WeTheme.typography.title,
                        color = WeTheme.colorScheme.fontColorHeavy,
                        modifier = Modifier.onGloballyPositioned {
                            paddingStart = it.size.width / 4
                        },
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                CompositionLocalProvider(
                    LocalWeTypography provides uiState.allWeTypography.last()
                ) {
                    Text(
                        text = stringResource(R.string.string_font_screen_example),
                        style = WeTheme.typography.title,
                        color = WeTheme.colorScheme.fontColorHeavy,
                        modifier = Modifier.onGloballyPositioned {
                            paddingEnd = it.size.width / 4
                        },
                    )
                }

            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(WeTheme.dimens.chatPadding / 2)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = max(paddingStart - 1, 0).dp,
                        end = max(paddingEnd - 2, 0).dp,
                    ),
            ) {
                FontSlide(
                    index = uiState.currentIndex,
                    max = uiState.allWeTypography.size - 1,
                    onValueChange = {
                        onAction(FontScreenAction.OnChangeTypography(it))
                    },
                )
            }
        }
    }
}

@Composable
private fun ChatMessageAvatar(
    isShow: Boolean = true
) {
    Box(modifier = Modifier.size(WeTheme.dimens.chatAvatarSize)) {
        if (isShow) {
            val imageRequest =
                ImageRequest.Builder(LocalContext.current).data("").diskCacheKey("").build()
            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                placeholder = painterResource(id = R.mipmap.ic_launcher),
                error = painterResource(id = R.mipmap.ic_launcher),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(WeTheme.dimens.chatAvatarRoundedCornerDp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun ChatMessageText(
    text: String, isSend: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        ChatMessageAvatar(isShow = !isSend)
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = if (isSend) Arrangement.End else Arrangement.Start
        ) {
            SelectionContainer {
                Text(
                    modifier = Modifier
                        .padding(horizontal = WeTheme.dimens.chatPadding)
                        .clip(RoundedCornerShape(WeTheme.dimens.chatAvatarRoundedCornerDp))
                        .background(if (isSend) WeTheme.colorScheme.chatMessageBackgroundSend else WeTheme.colorScheme.chatMessageBackgroundReceive)
                        .padding(WeTheme.dimens.chatPadding),
                    text = text,
                    style = WeTheme.typography.title,
                    color = if (isSend) WeTheme.colorScheme.chatMessageTextSend else WeTheme.colorScheme.chatMessageTextReceive
                )
            }
        }
        ChatMessageAvatar(isShow = isSend)
    }
}

@Composable
private fun FontSlide(
    index: Int,
    max: Int,
    onValueChange: (Int) -> Unit,
) {
    if (index < 0) return

    var trackWidth by remember { mutableIntStateOf(0) }
    
    // 使用 rememberUpdatedState 确保手势闭包内能拿到最新值而不触发 pointerInput 重启
    val currentIndex by rememberUpdatedState(index)
    val currentOnValueChange by rememberUpdatedState(onValueChange)
    
    // 记录拖动过程中的逻辑位置 (0.0 ~ 1.0)
    var dragPosition by remember { mutableFloatStateOf(0f) }

    val knobSize = WeTheme.dimens.actionIconSize
    val knobRadius = knobSize / 2
    
    // 视觉位置完全由当前 index 决定，实现“跳跃”效果
    val visualPos = index.toFloat() / max.coerceAtLeast(1)

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .height(knobSize)
            .onSizeChanged { trackWidth = it.width }
            .pointerInput(max) { // 注意：这里不要传 index
                detectTapGestures { offset ->
                    if (trackWidth > 0) {
                        val newIndex = (offset.x / trackWidth * max).roundToInt().coerceIn(0, max)
                        currentOnValueChange(newIndex)
                    }
                }
            }
            .pointerInput(max) { // 注意：这里不要传 index
                detectHorizontalDragGestures(
                    onDragStart = { 
                        // 开始拖动时，同步逻辑位置到当前 index 的位置
                        dragPosition = currentIndex.toFloat() / max.coerceAtLeast(1) 
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        if (trackWidth > 0) {
                            // 更新逻辑位置
                            dragPosition = (dragPosition + dragAmount / trackWidth).coerceIn(0f, 1f)
                            // 计算逻辑位置对应的 index
                            val nextIndex = (dragPosition * max).roundToInt()
                            // 如果计算出的 index 变了，则触发更新，滑块会随之跳跃
                            if (nextIndex != currentIndex) {
                                currentOnValueChange(nextIndex)
                            }
                        }
                    }
                )
            }
    ) {
        // 绘制背景刻度线
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(knobRadius),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            for (i in 0..max) {
                Spacer(
                    modifier = Modifier
                        .height(knobRadius)
                        .width(WeTheme.dimens.outlineHeight)
                        .background(WeTheme.colorScheme.fontColorLight),
                )
                if (i < max) {
                    Spacer(
                        modifier = Modifier
                            .height(WeTheme.dimens.outlineHeight)
                            .weight(1f)
                            .background(WeTheme.colorScheme.fontColorLight),
                    )
                }
            }
        }

        // 绘制滑块 (它的 offset 仅取决于 index，即 visualPos)
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        (trackWidth * visualPos).toInt() - knobRadius.roundToPx(), 0
                    )
                }
                .size(knobSize)
                .clip(CircleShape)
                .background(WeTheme.colorScheme.primaryButton)
        )
    }
}
