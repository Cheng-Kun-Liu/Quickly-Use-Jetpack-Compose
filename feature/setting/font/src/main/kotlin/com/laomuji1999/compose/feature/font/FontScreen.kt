package com.laomuji1999.compose.feature.font

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.typography.LocalWeTypography
import com.laomuji1999.compose.core.ui.we.typography.WeTypography14
import com.laomuji1999.compose.core.ui.we.widget.button.WeButton
import com.laomuji1999.compose.core.ui.we.widget.button.WeButtonColor
import com.laomuji1999.compose.core.ui.we.widget.button.WeButtonType
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutline
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutlineType
import com.laomuji1999.compose.core.ui.we.widget.scaffold.WeScaffold
import com.laomuji1999.compose.core.ui.we.widget.topbar.WeTopBar
import com.laomuji1999.compose.res.R
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
        onAction = viewModel::onAction
    )
}

@Composable
private fun FontScreenUi(
    uiState: FontScreenUiState,
    onAction: (FontScreenAction) -> Unit,
) {
    val weTypography = uiState.currentWeTypography ?: LocalWeTypography.current
    WeScaffold(
        topBar = {
            WeTopBar(
                title = stringResource(id = R.string.string_font_screen_title),
                onBackClick = {
                    onAction(FontScreenAction.OnClickBack)
                },
                actions = {
                    WeButton(
                        text = stringResource(id = R.string.string_font_screen_confirm),
                        weButtonType = WeButtonType.Warp,
                        weButtonColor = WeButtonColor.Primary,
                        onClick = {
                            onAction(FontScreenAction.OnConfirm)
                        }
                    )
                }
            )
            WeOutline(weOutlineType = WeOutlineType.Full)
        }
    ) {
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
        CompositionLocalProvider(
            LocalWeTypography provides WeTypography14
        ) {
            Column(
                modifier = Modifier
                    .background(WeTheme.colorScheme.rowBackground)
                    .fillMaxWidth()
                    .padding(WeTheme.dimens.chatPadding * 2)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = stringResource(R.string.string_font_screen_example),
                        style = WeTheme.typography.micro,
                        color = WeTheme.colorScheme.fontColorHeavy
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.string_font_screen_example),
                        style = WeTheme.typography.titleEm,
                        color = WeTheme.colorScheme.fontColorHeavy
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(WeTheme.dimens.chatPadding)
                )
                FontSlide(
                    index = uiState.currentIndex,
                    min = 0,
                    max = uiState.totalSize - 1,
                    onValueChange = {
                        onAction(FontScreenAction.OnChangeTypography(it))
                    }
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
            val imageRequest = ImageRequest
                .Builder(LocalContext.current)
                .data("")
                .diskCacheKey("")
                .build()
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
    text: String,
    isSend: Boolean
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
    min: Int,
    max: Int,
    onValueChange: (Int) -> Unit
) {
    if (index < 0) {
        return
    }

    var sliderPos by remember(index) {
        mutableFloatStateOf((index - min).toFloat() / (max - min).coerceAtLeast(1))
    }
    var trackWidth by remember { mutableIntStateOf(0) }
    val knobSize = WeTheme.dimens.actionIconSize
    val knobRadius = knobSize / 2

    Box(contentAlignment = Alignment.CenterStart) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(knobRadius)
                .clip(CircleShape)
                .background(WeTheme.colorScheme.secondaryButton)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        sliderPos = (sliderPos + dragAmount / trackWidth).coerceIn(0f, 1f)
                        onValueChange((min + (max - min) * sliderPos).roundToInt())
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        sliderPos = (offset.x / trackWidth).coerceIn(0f, 1f)
                        onValueChange((min + (max - min) * sliderPos).roundToInt())
                    }
                }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { trackWidth = it.width },
            contentAlignment = Alignment.CenterStart
        ) {
            Spacer(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (trackWidth * sliderPos).toInt() - knobRadius.roundToPx(),
                            0
                        )
                    }
                    .size(knobSize)
                    .clip(CircleShape)
                    .background(WeTheme.colorScheme.primaryButton)
            )
        }
    }
}
