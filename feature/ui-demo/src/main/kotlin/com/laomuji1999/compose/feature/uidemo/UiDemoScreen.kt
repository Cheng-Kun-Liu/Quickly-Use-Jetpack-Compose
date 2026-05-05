package com.laomuji1999.compose.feature.uidemo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.laomuji1999.compose.core.ui.navigation.AppNavigationAction
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.core.ui.view.BannerDemoView
import com.laomuji1999.compose.core.ui.view.DragListDemo
import com.laomuji1999.compose.core.ui.we.WeDialog
import com.laomuji1999.compose.core.ui.we.widget.click.WeClick
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutline
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutlineType
import com.laomuji1999.compose.core.ui.we.widget.scaffold.WeScaffold
import com.laomuji1999.compose.core.ui.we.widget.switc.WeSwitch
import com.laomuji1999.compose.core.ui.we.widget.title.WeTitle
import com.laomuji1999.compose.res.R

@Composable
fun UiDemoScreen(
    viewModel: UiDemoScreenViewModel = hiltViewModel(),
    showSimpleDragView: Boolean,
    onShowSimpleDragViewChange: (Boolean) -> Unit,
    onAction: (AppNavigationAction) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showDragListDialog by rememberSaveable {
        mutableStateOf(false)
    }
    if (showDragListDialog) {
        WeDialog(
            onDismissRequest = {
                showDragListDialog = false
            },
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            DragListDemo(
                dragList = uiState.dragList,
                onSwap = { a, b ->
                    viewModel.onAction(UiDemoScreenAction.SwapDragList(a, b))
                },
            )
        }
    }

    UiDemoScreenUi(
        showSimpleDragView = showSimpleDragView,
        onShowSimpleDragViewChange = onShowSimpleDragViewChange,
        onLongClickSortClick = {
            showDragListDialog = true
        },
        onAction = onAction,
    )
}

@Composable
private fun UiDemoScreenUi(
    showSimpleDragView: Boolean,
    onShowSimpleDragViewChange: (Boolean) -> Unit,
    onLongClickSortClick: () -> Unit,
    onAction: (AppNavigationAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        WeTitle(title = stringResource(id = R.string.string_demo_screen_banner))
        WeOutline(weOutlineType = WeOutlineType.Full)
        BannerDemoView()
        WeOutline(weOutlineType = WeOutlineType.Split)

        WeTitle(title = stringResource(id = R.string.string_demo_group_ui_interaction))
        WeOutline(weOutlineType = WeOutlineType.Full)
        WeSwitch(
            title = stringResource(id = R.string.string_demo_screen_simple_drag_view),
            checked = showSimpleDragView,
            onCheckedChange = onShowSimpleDragViewChange,
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)
        WeClick(
            title = stringResource(id = R.string.string_demo_screen_long_click_sort),
            onClick = onLongClickSortClick,
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)
        WeClick(
            title = stringResource(id = R.string.string_demo_screen_date),
            onClick = {
                onAction(AppNavigationAction.OnDateClick)
            },
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)
        WeClick(
            title = "${stringResource(id = R.string.string_demo_screen_scroll_connect)}\n${
                stringResource(
                    id = R.string.string_demo_screen_scroll_dispatcher
                )
            }",
            onClick = {
                onAction(AppNavigationAction.OnNestedScrollConnectionScreenClick)
            },
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)
        WeClick(
            title = stringResource(id = R.string.string_demo_screen_painter),
            onClick = {
                onAction(AppNavigationAction.OnPainterScreenClick)
            },
        )
        WeOutline(weOutlineType = WeOutlineType.Full)
    }
}

@Preview
@Composable
private fun PreviewUiDemoScreen() {
    QuicklyTheme {
        WeScaffold {
            UiDemoScreenUi(
                showSimpleDragView = true,
                onShowSimpleDragViewChange = {},
                onLongClickSortClick = {},
                onAction = {},
            )
        }
    }
}
