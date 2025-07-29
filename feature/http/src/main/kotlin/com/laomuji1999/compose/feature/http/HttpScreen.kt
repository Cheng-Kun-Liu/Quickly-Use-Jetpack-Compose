package com.laomuji1999.compose.feature.http

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.core.ui.view.ErrorView
import com.laomuji1999.compose.core.ui.view.LoadingDialog
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.icons.Loading
import com.laomuji1999.compose.core.ui.we.icons.WeIcons
import com.laomuji1999.compose.core.ui.we.widget.button.WeButton
import com.laomuji1999.compose.core.ui.we.widget.button.WeButtonColor
import com.laomuji1999.compose.core.ui.we.widget.button.WeButtonType
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutline
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutlineType
import com.laomuji1999.compose.core.ui.we.widget.row.WeRow
import com.laomuji1999.compose.core.ui.we.widget.scaffold.WeScaffold
import com.laomuji1999.compose.core.ui.we.widget.topbar.WeTopBar
import com.laomuji1999.compose.res.R
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun HttpScreen(
    viewModel: HttpScreenViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isError) {
        ErrorView {
            HttpScreen(
                viewModel = hiltViewModel(key = uniqueKey()),
            )
        }
        return
    }

    LoadingDialog(loading = uiState.isLoading)

    HttpScreenUi(
        uiState = uiState, onAction = viewModel::onAction
    )
}

@Composable
private fun HttpScreenUi(
    uiState: HttpScreenUiState,
    onAction: (HttpScreenAction) -> Unit,
) {
    WeScaffold(
        topBar = {
            WeTopBar(
                title = stringResource(id = R.string.string_demo_screen_http_demo),
            )
        }) {
        Text(
            text = stringResource(id = R.string.string_http_screen_is_online, uiState.isConnect),
            style = WeTheme.typography.titleEm,
            color = WeTheme.colorScheme.fontColorHeavy,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(WeTheme.dimens.chatPadding))
        HttpScreenSlot(
            text = stringResource(id = R.string.string_http_screen_get_demo),
            onClick = { onAction(HttpScreenAction.GetListUsers) })
        HttpScreenSlot(
            text = stringResource(id = R.string.string_http_screen_post_demo),
            onClick = { onAction(HttpScreenAction.CreateUser) })
        Text(
            text = uiState.responseText,
            color = WeTheme.colorScheme.fontColorHeavy,
            style = WeTheme.typography.micro,
        )
        ProductList(
            uiState = uiState,
            onAction = onAction,
        )
    }
}

@Composable
private fun HttpScreenSlot(
    text: String,
    onClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        WeButton(
            weButtonType = WeButtonType.Big, weButtonColor = WeButtonColor.Primary, text = text
        ) {
            onClick()
        }
    }
    Spacer(modifier = Modifier.height(WeTheme.dimens.chatPadding))
}

@Composable
private fun ProductList(
    uiState: HttpScreenUiState,
    onAction: (HttpScreenAction) -> Unit,
) {
    val lazyColumnState = rememberLazyListState()
    LaunchedEffect(uiState.products) {
        snapshotFlow { lazyColumnState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }.distinctUntilChanged()
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex == uiState.products.lastIndex) {
                    onAction(HttpScreenAction.LoadMore)
                }
            }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyColumnState,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(items = uiState.products) {
            Column {
                WeRow(
                    center = {
                        Column(
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                text = it.title,
                                style = WeTheme.typography.title,
                                color = WeTheme.colorScheme.fontColorHeavy,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Spacer(modifier = Modifier.height(WeTheme.dimens.warpButtonVerticalPaddingDp))
                            Text(
                                text = it.description,
                                style = WeTheme.typography.body,
                                color = WeTheme.colorScheme.fontColorLight,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }

                    },
                    end = {
                        Text(
                            text = "${it.price}",
                            style = WeTheme.typography.caption,
                            color = WeTheme.colorScheme.fontColorHeavy,
                        )
                    }
                )
                WeOutline(
                    weOutlineType = WeOutlineType.PaddingHorizontal
                )
            }
        }
        if (uiState.isLoadMore) {
            item {
                Spacer(modifier = Modifier.height(WeTheme.dimens.chatPadding))
                val infiniteTransition = rememberInfiniteTransition(label = "WeToastType.Loading")
                val rotateDegree by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 1000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "WeToastType.Loading"
                )
                Image(
                    imageVector = WeIcons.Loading,
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .size(WeTheme.dimens.toastIconSize)
                        .rotate(rotateDegree),
                    colorFilter = ColorFilter.tint(WeTheme.colorScheme.primaryButton)
                )
                Spacer(modifier = Modifier.height(WeTheme.dimens.chatPadding))
            }
        }
    }
}

@Preview
@Composable
fun PreviewHttpScreenUi() {
    QuicklyTheme {
        HttpScreenUi(
            uiState = HttpScreenUiState(), onAction = {})
    }
}

