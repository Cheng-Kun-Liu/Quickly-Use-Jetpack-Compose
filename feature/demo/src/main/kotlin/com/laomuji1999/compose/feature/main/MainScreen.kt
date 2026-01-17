package com.laomuji1999.compose.feature.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.laomuji1999.compose.core.ui.isPreview
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
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
    WeScaffold(topBar = {
        WeTopBar(
            title = when (MainScreenPageEnum.entries[pagerState.currentPage]) {
                MainScreenPageEnum.FEATURE -> stringResource(id = R.string.string_main_screen_page_features)
                MainScreenPageEnum.UI -> stringResource(id = R.string.string_main_screen_page_ui)
                MainScreenPageEnum.SETTINGS -> stringResource(id = R.string.string_main_screen_page_settings)
            }
        )
    }, bottomBar = {
        MainScreenBottomBar(
            selectedEnum = MainScreenPageEnum.entries[pagerState.currentPage],
            onSelectedEnumChange = {
                coroutineScope.launch {
                    pagerState.scrollToPage(it.ordinal)
                }
            })
    }) {
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

@Preview
@Composable
private fun PreviewMainScreenUi() {
    QuicklyTheme {
        MainScreenUi(onAction = {})
    }
}