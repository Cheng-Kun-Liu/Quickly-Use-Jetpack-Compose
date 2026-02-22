package com.laomuji1999.compose.feature.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.laomuji1999.compose.core.ui.extension.isPreview
import com.laomuji1999.compose.core.ui.extension.modifier.ModifierDraggable.draggable
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.icons.Device
import com.laomuji1999.compose.core.ui.we.icons.Feature
import com.laomuji1999.compose.core.ui.we.icons.Ui
import com.laomuji1999.compose.core.ui.we.icons.WeIcons
import com.laomuji1999.compose.core.ui.we.widget.bottombar.WeBottomBar
import com.laomuji1999.compose.core.ui.we.widget.bottombar.WeBottomBarItem
import com.laomuji1999.compose.core.ui.we.widget.scaffold.WeScaffold
import com.laomuji1999.compose.core.ui.we.widget.topbar.WeTopBar
import com.laomuji1999.compose.feature.main.MainScreenRoute.Graph
import com.laomuji1999.compose.feature.main.feature.FeatureScreen
import com.laomuji1999.compose.feature.main.settings.SettingsScreen
import com.laomuji1999.compose.feature.main.ui.UiDemoScreen
import com.laomuji1999.compose.res.R
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
    navigateToGraph: (Graph) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.graph.collect {
            navigateToGraph(it)
        }
    }
    MainScreenUi(
        onAction = viewModel::onAction
    )
}

@Composable
private fun MainScreenUi(
    onAction: (MainScreenAction) -> Unit
) {
    val pagerState = rememberPagerState(
        pageCount = { MainScreenPageEnum.entries.size })
    val coroutineScope = rememberCoroutineScope()
    WeScaffold(
        topBar = {
            WeTopBar(
                title = when (MainScreenPageEnum.entries[pagerState.currentPage]) {
                    MainScreenPageEnum.FEATURE -> stringResource(id = R.string.string_main_screen_page_features)
                    MainScreenPageEnum.UI -> stringResource(id = R.string.string_main_screen_page_ui)
                    MainScreenPageEnum.SETTINGS -> stringResource(id = R.string.string_main_screen_page_settings)
                }
            )
        },
        bottomBar = {
            MainScreenBottomBar(
                selectedEnum = MainScreenPageEnum.entries[pagerState.currentPage],
                onSelectedEnumChange = {
                    coroutineScope.launch {
                        pagerState.scrollToPage(it.ordinal)
                    }
                })
        },
        extra = {
            SimpleDragView()
        }
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = if (isPreview()) 0 else MainScreenPageEnum.entries.size
        ) {
            when (MainScreenPageEnum.entries[it]) {
                MainScreenPageEnum.FEATURE -> FeatureScreen(onAction = onAction)

                MainScreenPageEnum.UI -> UiDemoScreen(onAction = onAction)

                MainScreenPageEnum.SETTINGS -> SettingsScreen(onAction = onAction)
            }
        }
    }
}

@Composable
private fun MainScreenBottomBar(
    selectedEnum: MainScreenPageEnum,
    onSelectedEnumChange: (MainScreenPageEnum) -> Unit,
) {
    WeBottomBar {
        WeBottomBarItem(
            unSelectImageVector = WeIcons.Feature,
            title = stringResource(id = R.string.string_main_screen_page_features),
            selected = selectedEnum == MainScreenPageEnum.FEATURE,
            onClick = {
                onSelectedEnumChange(MainScreenPageEnum.FEATURE)
            })
        WeBottomBarItem(
            unSelectImageVector = WeIcons.Ui,
            title = stringResource(id = R.string.string_main_screen_page_ui),
            selected = selectedEnum == MainScreenPageEnum.UI,
            onClick = {
                onSelectedEnumChange(MainScreenPageEnum.UI)
            })
        WeBottomBarItem(
            unSelectImageVector = WeIcons.Device,
            title = stringResource(id = R.string.string_main_screen_page_settings),
            selected = selectedEnum == MainScreenPageEnum.SETTINGS,
            onClick = {
                onSelectedEnumChange(MainScreenPageEnum.SETTINGS)
            })
    }
}

@Composable
private fun SimpleDragView() {
    val iconSize = WeTheme.dimens.toastIconSize * 2
    val density = LocalDensity.current
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        val offsetX = with(density) { (375.dp - iconSize).toPx() }
        Image(
            painter = painterResource(R.mipmap.ic_launcher),
            contentDescription = null,
            modifier = Modifier
                .size(iconSize)
                .draggable(
                    initOffsetX = offsetX,
                    initOffsetY = with(density) { 300.dp.toPx() },
                    enableSnap = true,
                )
        )
    }
}

@Preview
@Composable
private fun PreviewMainScreenUi() {
    QuicklyTheme {
        MainScreenUi(onAction = {})
    }
}